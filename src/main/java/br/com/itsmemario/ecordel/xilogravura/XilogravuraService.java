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

package br.com.itsmemario.ecordel.xilogravura;

import br.com.itsmemario.ecordel.file.FileManager;
import br.com.itsmemario.ecordel.file.FileProcessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class XilogravuraService {

  private static final String JPG = ".jpg";

  private FileManager fileManager;

  @Autowired
  public XilogravuraService(FileManager fileManager) {
    this.fileManager = fileManager;
  }

  public String createXilogravuraWithFile(MultipartFile file) {
    try {
      var fileName = generateRandomFileName();
      return fileManager.saveFile(file.getBytes(), fileName);
    } catch (IOException e) {
      throw new FileProcessException("Error while saving file", e);
    }
  }

  private String generateRandomFileName() {
    return UUID.randomUUID() + JPG;
  }

}
