package com.example.BookApp.search;

import com.example.BookApp.search.BookDto;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BookController {
    @Value("${naver-client-id}")
    private String clientId;
    @Value("${naver-client-secret}")
    private String clientSecret;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("board", new BookDto());
        return "addForm";
    }

    @GetMapping("/book-search")
    public String BookSearchHome(Model model) {
        String keyword = "";
        model.addAttribute("keyword", keyword);
        return "bookSearch";
    }

    @PostMapping("/book-search")
    public String search(@ModelAttribute("keyword") String keyword,Model model){
        try{
            String encodeKeyword= URLEncoder.encode(keyword,"UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/book?query=" + encodeKeyword;
            URL url = new URL(apiURL);
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret",clientSecret);
            int responseCode = con.getResponseCode();//호출
            BufferedReader bf;
            InputStream inputStream=con.getInputStream();
            InputStream errorStream=con.getErrorStream();



            if(responseCode==200){//정상호출
                bf=new BufferedReader(new InputStreamReader(inputStream));
            }
            else {
                bf=new BufferedReader(new InputStreamReader(errorStream));
            }
            String line;
            //StringBuilder responseBody=new StringBuilder();
            StringBuffer response=new StringBuffer();
            while((line=bf.readLine())!=null){
                response.append(line);
            }
            bf.close();

            JSONParser jsonParse=new JSONParser();

            //Read객체 다루기
            JSONObject jsonObject=(JSONObject) jsonParse.parse(response.toString());
            //json array만들기
            JSONArray jsArr=(JSONArray) jsonObject.get("items");

            List<BookDto> bookDtoList=new ArrayList<>();

            for(int i=0;i<jsArr.size();i++){

                JSONObject object = (JSONObject) jsArr.get(i);
                String title=(String)object.get("title");
                String author=(String)object.get("author");
                String image=(String) object.get("image");

                bookDtoList.add(BookDto.builder().
                        title(title).author(author).imageURL(image).build());

            }

            model.addAttribute("bookDtoList",bookDtoList);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "bookSearch";
    }
}
