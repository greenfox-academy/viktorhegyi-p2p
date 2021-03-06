package com.greenfox.chat.controller;

import com.greenfox.chat.model.Client;
import com.greenfox.chat.model.Json;
import com.greenfox.chat.model.Message;
import com.greenfox.chat.model.NameOfUser;
import com.greenfox.chat.service.LogMessageService;
import com.greenfox.chat.repository.MessageRepo;
import com.greenfox.chat.repository.UserRepo;
import java.sql.Timestamp;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Viktor on 2017-05-17.
 */
@Controller
public class MainController {

  @Autowired
  LogMessageService logMessageService;
  @Autowired
  UserRepo userRepo;
  @Autowired
  NameOfUser nameOfUser;
  @Autowired
  MessageRepo messageRepo;
  @Autowired
  Message message;

  RestTemplate restTemplate = new RestTemplate();

  @ModelAttribute
  private void logInfo(HttpServletRequest httpServletRequest) {
    logMessageService.getInfo(httpServletRequest);
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String index(Model model) {
    model.addAttribute("user", userRepo.findOne(1l));
    model.addAttribute("messages", messageRepo.findAllByOrderByTimestampDesc());
    return "index";
  }

  @RequestMapping(value = "/enter", method = RequestMethod.GET)
  public String register() {
    return "enter";
  }

  @PostMapping(value = "/enter")
  public String addUser(String username) {
    nameOfUser.setId(1l);
    nameOfUser.setNameOfUser(username);
    userRepo.save(nameOfUser);
    return "redirect:/";
  }

  @PostMapping(value = "/send")
  public String sendMessage(String currentMessage) {
    message.setId();
    message.setUsername(userRepo.findOne(1l).toString());
    message.setText(currentMessage);
    message.setTimestamp(new Timestamp(System.currentTimeMillis()));
    messageRepo.save(message);

    Client client = new Client();
    client.setId(System.getenv("CHAT_APP_UNIQUE_ID"));
    Json json = new Json();
    json.setMessage(message);
    json.setClient(client);

    try{
      restTemplate.postForObject(System.getenv("CHAT_APP_PEER_ADDRESSS"), json, Json.class);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return "redirect:/";
  }

  @RequestMapping(value = "/delete")
  public String delete() {
    messageRepo.deleteAll();
    return "redirect:/";
  }

}
