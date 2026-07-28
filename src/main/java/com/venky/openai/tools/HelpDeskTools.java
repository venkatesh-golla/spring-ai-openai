package com.venky.openai.tools;

import com.venky.openai.entity.HelpDeskTicket;
import com.venky.openai.model.TicketRequest;
import com.venky.openai.service.HelpDeskTicketService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HelpDeskTools {
  private static final Logger logger = LoggerFactory.getLogger(HelpDeskTools.class);
  private final HelpDeskTicketService helpDeskTicketService;

  @Tool(name="createTicket", description = "Create a support ticket", returnDirect = true)
  String createTicket(
      @ToolParam(description = "Details to create a support ticket") TicketRequest ticketRequest,
      ToolContext context) {
      String username = context.getContext().get("username").toString();
      logger.info("Creating ticket for user: {}", username);
      HelpDeskTicket ticket = helpDeskTicketService.createTicket(ticketRequest, username);
      logger.info("Ticket created successfully: {}", ticket);
      return "Ticket created successfully. Ticket ID: " + ticket.getId() + " for user: " + username;
  }

  @Tool(name="getTicketStatus", description = "Get the status of open support tickets based on a given username")
  List<HelpDeskTicket> getTicketStatus(ToolContext context) {
      String username = context.getContext().get("username").toString();
      logger.info("Fetching ticket status for user: {}", username);
      List<HelpDeskTicket> tickets = helpDeskTicketService.getTicketsByUserName(username);
      logger.info("Found {} tickets for user: {}", tickets.size(), username);
      return tickets;
  }
}
