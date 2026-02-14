package com.eazybytes.openai.tools;

import com.eazybytes.openai.entity.HelpDeskTicket;
import com.eazybytes.openai.model.TicketRequest;
import com.eazybytes.openai.service.HelpDeskTicketService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HelpDeskTools {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelpDeskTools.class);

    private final HelpDeskTicketService helpDeskTicketService;

    @Tool(name = "createTicket", description = "Create the Support Ticket")
    String createTicket(@ToolParam(description = "Details to create a Support ticket") TicketRequest ticketRequest, ToolContext toolContext) {
        String username = (String) toolContext.getContext().get("username");
        HelpDeskTicket ticket = helpDeskTicketService.createTicket(ticketRequest, username);
        return "Ticket #" + ticket.getId() + " created successfully for user" + ticket.getUsername();
    }

    @Tool(name = "getTickets", description = "Get the list of tickets for the user")
    List<HelpDeskTicket> getTickets(ToolContext toolContext) {
        String username = (String) toolContext.getContext().get("username");
        return helpDeskTicketService.getTickets(username);
    }
}
