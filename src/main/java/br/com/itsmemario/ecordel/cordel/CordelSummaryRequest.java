/*
 * Copyright 2025 Projeto e-cordel (http://ecordel.com.br)
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

package br.com.itsmemario.ecordel.cordel;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * CordelSummaryRequest class represents a request for a summary of a Cordel.
 * It contains fields for title, published status, and author ID.
 * It also provides methods to format the title and get the published status.
 */
@Data
@Builder
@AllArgsConstructor
public class CordelSummaryRequest {

  private String title;
  private Boolean published;
  private Long authorId;

  public String getFormatedTitle() {
    return String.format("%%%s%%", this.title);
  }

  public Boolean getPublished() {
    if (Objects.isNull(published)) {
      return true;
    }
    return published;
  }
}
