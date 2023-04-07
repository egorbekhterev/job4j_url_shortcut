package ru.job4j.urlshortcut.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.urlshortcut.UrlShortcutApplication;
import ru.job4j.urlshortcut.dto.url.LongUrlDTO;
import ru.job4j.urlshortcut.dto.url.ShortUrlDTO;
import ru.job4j.urlshortcut.model.URL;
import ru.job4j.urlshortcut.service.UrlService;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = UrlShortcutApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
public class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlService urlService;

    @Test
    @WithMockUser
    public void whenConvertValidUrlThenGetShortUrlAnd200Status() throws Exception {
        var json = new JSONObject();
        json.put("longUrl", "https://job4j.ru/profile/exercise/106/task/532");

        var url = new ShortUrlDTO();
        url.setShortUrl("b96867");

        ArgumentCaptor<LongUrlDTO> argument = ArgumentCaptor.forClass(LongUrlDTO.class);
        Mockito.when(urlService.convert(argument.capture())).thenReturn(url);

        mockMvc.perform(post("/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("shortUrl").value("b96867"));
    }

    @Test
    @WithMockUser
    public void whenConvertWithNotValidArgumentFirst() throws Exception {
        var json = new JSONObject();
        json.put("longUrl", "goolge.com");

        mockMvc.perform(post("/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString())).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void whenConvertWithNotValidArgumentSecond() throws Exception {
        var json = new JSONObject();
        json.put("longUrl", "https://google   .com");

        mockMvc.perform(post("/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString())).andExpect(status().isBadRequest());
    }

    @Test
    public void whenRedirectThenGetPageAnd302Status() throws Exception {
        var shortUrl = "test";
        var url = new URL();
        url.setLongUrl("https://job4j/profile/exercise/106/task/532");

        Mockito.when(urlService.findByShortUrl(any())).thenReturn(Optional.of(url));

        mockMvc.perform(get("/redirect/" + shortUrl))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(url.getLongUrl()));
    }

    @Test
    public void whenRedirectThenShortUrlNotFound() throws Exception {
        var shortUrl = "test";
        Mockito.when(urlService.findByShortUrl(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/redirect/" + shortUrl))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testStatistics() throws Exception {
        mockMvc.perform(get("/statistic"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}