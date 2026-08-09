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

/**
 * Response payload for AI-assisted cordel review.
 *
 * @param completed indicates whether the AI review is completed.
 * @param content revised cordel text returned by the model.
 */
public record AiReviewResponse(boolean completed, String content) {
}
