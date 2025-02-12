package com.springweb.oauthsession.dto;

public interface OAuth2Response {

    String getProvider();//제공자( 구글, 네이버, 카카오 등)

    String getProviderId();

    String getEmail();

    String getName();
}
