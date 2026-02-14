package com.eazybytes.openai.service;

import com.eazybytes.openai.entity.HelpDeskTicket;
import com.eazybytes.openai.model.TicketRequest;
import com.eazybytes.openai.repository.HelpDeskTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HelpDeskTicketService {
    private final HelpDeskTicketRepository helpDeskTicketRepository;

    public HelpDeskTicket createTicket(TicketRequest ticketRequest, String username) {
        HelpDeskTicket ticket = HelpDeskTicket.builder()
                .issue(ticketRequest.issue())
                .username(username)
                .status("OPEN")
                .createdAt(LocalDateTime.now())
                .eta(LocalDateTime.now().plusDays(7))
                .build();

        return helpDeskTicketRepository.save(ticket);
    }

    public List<HelpDeskTicket> getTickets(String username) {
        return helpDeskTicketRepository.findByUsername(username);
    }
}
