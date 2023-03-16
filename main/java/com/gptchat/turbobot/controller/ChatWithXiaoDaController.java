package com.gptchat.turbobot.controller;

import com.gptchat.turbobot.api.chat.ChatFeignApi;
import com.gptchat.turbobot.dto.chat.InputDto;
import com.gptchat.turbobot.service.chat.ChatWithXiaoDa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class ChatWithXiaoDaController implements ChatFeignApi {

    @Autowired
    private ChatWithXiaoDa chatWithXiaoDa;

    @Override
    public String chat(InputDto inputDto) {
        return chatWithXiaoDa.getResponse(inputDto.getInput());
    }
}
