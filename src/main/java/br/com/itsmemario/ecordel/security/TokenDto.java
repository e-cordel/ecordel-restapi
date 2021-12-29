/*
 * Copyright 2020 Projeto e-cordel (http://ecordel.com.br)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package br.com.itsmemario.ecordel.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenDto {
	
	private final String token;
	private final String authenticationMethod;
	private final Long expiresAt;

	@JsonCreator
	public TokenDto(@JsonProperty("token") String token,
					@JsonProperty("authenticationMethod") String authenticationMethod,
					@JsonProperty("expiresAt") Long expiresAt) {
		super();
		this.token = token;
		this.authenticationMethod = authenticationMethod;
		this.expiresAt = expiresAt;
	}

	public String getToken() {
		return token;
	}

	public String getAuthenticationMethod() {
		return authenticationMethod;
	}

	public Long getExpiresAt() {
		return expiresAt;
	}

	@Override
	public String toString() {
		return authenticationMethod + " " + token;
	}
}
