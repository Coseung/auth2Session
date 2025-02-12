package com.springweb.oauthsession.service;

import com.springweb.oauthsession.dto.CustomOAuth2User;
import com.springweb.oauthsession.dto.NaverResponse;
import com.springweb.oauthsession.dto.OAuth2Response;
import com.springweb.oauthsession.entity.UserEntity;
import com.springweb.oauthsession.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    //사용자정보 데이터를 인자로 받아오는 매소드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = super.loadUser(userRequest);//유저정보가져오기
        System.out.println(oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();//구글, 카카오, 네이버 구분

        OAuth2Response oAuth2Response = null;
        if(registrationId.equals("naver")){

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
            //바구니에서 데이터를 뽑아온것.

        } else{
            return null;
        }

        //customOAuth2User에서 만들어놓은 매소드를 활용 하여 username 데이터 넣기
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        UserEntity existData = userRepository.findByUsername(username);
        String role = null;

        if(existData == null){
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(username);//위의 변수 에서 집어넣기
            userEntity.setEmail(oAuth2Response.getEmail());//oAuth2Response에서 가져오기
            userEntity.setRole("ROLE_USER");
            userRepository.save(userEntity);
        }else{
            role = existData.getRole();
            existData.setEmail(oAuth2Response.getEmail());
            //Email이 바뀔 수 있으니 업테이트
            userRepository.save(existData);
        }


        return new CustomOAuth2User(oAuth2Response,role);
    }
}
