/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2013-03-20 15:12:36 UTC)
 * on 2013-03-25 at 22:04:24 UTC 
 * Modify at your own risk.
 */

package com.google.api.services.supersketchysketch.model;

/**
 * Model definition for SRLQuestion.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the . For a detailed explanation see:
 * <a href="http://code.google.com/p/google-api-java-client/wiki/Json">http://code.google.com/p/google-api-java-client/wiki/Json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class SRLQuestion extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long id;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String question;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("srl_assignmentId") @com.google.api.client.json.JsonString
  private java.lang.Long srlAssignmentId;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getId() {
    return id;
  }

  /**
   * @param id id or {@code null} for none
   */
  public SRLQuestion setId(java.lang.Long id) {
    this.id = id;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getQuestion() {
    return question;
  }

  /**
   * @param question question or {@code null} for none
   */
  public SRLQuestion setQuestion(java.lang.String question) {
    this.question = question;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getSrlAssignmentId() {
    return srlAssignmentId;
  }

  /**
   * @param srlAssignmentId srlAssignmentId or {@code null} for none
   */
  public SRLQuestion setSrlAssignmentId(java.lang.Long srlAssignmentId) {
    this.srlAssignmentId = srlAssignmentId;
    return this;
  }

  @Override
  public SRLQuestion set(String fieldName, Object value) {
    return (SRLQuestion) super.set(fieldName, value);
  }

  @Override
  public SRLQuestion clone() {
    return (SRLQuestion) super.clone();
  }

}