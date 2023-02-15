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

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.*;

@Entity
@Table(name = "cordel_authority")
public class CordelAuthority implements GrantedAuthority{
	
	public static final String COMMON_USER = "USER";
	public static final String ADMIN = "ADMIN";
	public static final String EDITOR = "EDITOR";
	public static final String AUTHOR = "AUTHOR";

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String authority;
	
	CordelAuthority() {}

	public CordelAuthority(String authority) {
		this.authority = authority;
	}

	@Override
	public String getAuthority() {
		return authority;
	}
	
	public static CordelAuthority commonUser() {
		return new CordelAuthority(COMMON_USER);
	}

}
