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

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
public class LoginData {
	
	private String username;
	private String password;
	
	public LoginData() {}
	
	public LoginData(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

    public UsernamePasswordAuthenticationToken toAuthenticationToken() {
		return new UsernamePasswordAuthenticationToken(username, password);
	}

}
