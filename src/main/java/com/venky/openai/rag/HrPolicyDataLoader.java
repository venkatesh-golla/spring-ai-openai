package com.venky.openai.rag;

import jakarta.annotation.PostConstruct;
import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class HrPolicyDataLoader {
  private final VectorStore vectorStore;

  @Value("classpath:Sample_HR_Policies.pdf")
  Resource pdfFile;

  public HrPolicyDataLoader(VectorStore vectorStore) {
    this.vectorStore = vectorStore;
  }

  @PostConstruct
  public void loadPdfIntoVectorStore() {
    TikaDocumentReader docReader = new TikaDocumentReader(pdfFile);
    List<Document> documents = docReader.get();
    TextSplitter textSplitter =
        TokenTextSplitter.builder().withChunkSize(100).withMaxNumChunks(400).build();
    vectorStore.add(textSplitter.split(documents));
  }
}
