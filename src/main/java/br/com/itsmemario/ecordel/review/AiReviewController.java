/*
 * Copyright 2026 Projeto e-cordel (http://ecordel.com.br)
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

package br.com.itsmemario.ecordel.review;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ai-reviews")
@RequiredArgsConstructor
public class AiReviewController {

  private final AiReviewService aiReviewService;

  @GetMapping(value = "{reviewId}")
  public ResponseEntity<AiReviewResponse> getReview(@PathVariable String reviewId) {
    var review = aiReviewService.getReview(reviewId);

    if (review.isEmpty()) {
      return ResponseEntity.notFound().build();
    } else {

      // cache entry exists but review text is not ready yet
      if (!review.get().completed()) {
        return ResponseEntity.accepted().build();
      }

      return ResponseEntity.ok(review.get());
    }
  }
}
