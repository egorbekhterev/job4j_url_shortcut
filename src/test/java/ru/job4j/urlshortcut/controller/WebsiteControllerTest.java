package ru.job4j.urlshortcut.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.urlshortcut.UrlShortcutApplication;
import ru.job4j.urlshortcut.model.Website;
import ru.job4j.urlshortcut.service.WebsiteService;

@SpringBootTest(classes = UrlShortcutApplication.class)
@AutoConfigureMockMvc
public class WebsiteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WebsiteService websiteService;

    @Test
    public void whenSignUpThenReturnWebsiteDTOAnd201Status() throws Exception {
        var json = new JSONObject();
        json.put("domainName", "job4j.ru");

        var website = new Website();
        website.setLogin("12345678");
        website.setPassword("123456");

        ArgumentCaptor<Website> argument = ArgumentCaptor.forClass(Website.class);
        Mockito.when(websiteService.save(argument.capture())).thenReturn(website);

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("registration").value(true))
                .andExpect(jsonPath("login").value("12345678"))
                .andExpect(jsonPath("password").value("123456"));
    }

    @Test
    public void whenSignUpWithNotValidArgument() throws Exception {
        var json = new JSONObject();
        json.put("domainName", "job4j");

        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.toString())).andExpect(status().isBadRequest());
    }
}
