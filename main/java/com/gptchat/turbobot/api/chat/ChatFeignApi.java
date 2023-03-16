package com.gptchat.turbobot.api.chat;

import com.gptchat.turbobot.dto.chat.InputDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @since 2023-3-16
 * @author ZHOUDA
 */
@RequestMapping("/turbot/chat")
public interface ChatFeignApi {

    /**
     * @param inputDto dto
     * @return resp
     */
    @PostMapping
    String chat(@RequestBody InputDto inputDto);
}
