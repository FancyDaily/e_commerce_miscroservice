	/*
	 * Copyright 2002-2012 the original author or authors.
	 *
	 * Licensed under the Apache License, Version 2.0 (the "License");
	 * you may not use this file except in compliance with the License.
	 * You may obtain a copy of the License at
	 *
	 *      http://www.apache.org/licenses/LICENSE-2.0
	 *
	 * Unless required by applicable law or agreed to in writing, software
	 * distributed under the License is distributed on an "AS IS" BASIS,
	 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 * See the License for the specific language governing permissions and
	 * limitations under the License.
	 */

	package com.e_commerce.miscroservice.commons.utils;

    import org.apache.commons.logging.Log;
    import org.apache.commons.logging.LogFactory;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.*;

    /**
     * MultipartFile implementation for Jakarta Commons FileUpload.
     *
     * @author Trevor D. Cook
     * @author Juergen Hoeller
     * @since 29.09.2003
     */
    @SuppressWarnings("serial")
    public class JiuyMultipartFile implements MultipartFile, Serializable {

        protected static final Log logger = LogFactory.getLog(JiuyMultipartFile.class);

        private final File file;

        private final long size;


        /**
         * Create an instance wrapping the given FileItem.
         */
        public JiuyMultipartFile(File file) {
            this.file = file;
            this.size = this.file.length();
        }

        @Override
        public String getName() {
            return this.file.getName();
        }

        @Override
        public String getOriginalFilename() {
            String filename = this.file.getName();
            if (filename == null) {
                // Should never happen.
                return "";
            }
            // check for Unix-style path
            int pos = filename.lastIndexOf("/");
            if (pos == -1) {
                // check for Windows-style path
                pos = filename.lastIndexOf("\\");
            }
            if (pos != -1)  {
                // any sort of path separator found
                return filename.substring(pos + 1);
            }
            else {
                // plain name
                return filename;
            }
        }

        @Override
        public String getContentType() {
            return null;
        }

        @Override
        public boolean isEmpty() {
            return (this.size == 0);
        }

        @Override
        public long getSize() {
            return this.size;
        }

        @Override
        public byte[] getBytes() {
            int nRet = 0;
            if (!isAvailable()) {
                throw new IllegalStateException("File has been moved - cannot be read again");
            }
            byte bytes[] = new byte[(int)this.size];
            FileInputStream iss = null;
            try {
                 iss = new FileInputStream (file);

                 nRet = iss.read(bytes);
            } catch (Exception e) {
            }
            finally {
                if (iss != null) {
                    try {
                        iss.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return (nRet > 0 ? bytes : new byte[0]);
        }

        @Override
        public InputStream getInputStream() throws IOException {
            if (!isAvailable()) {
                throw new IllegalStateException("File has been moved - cannot be read again");
            }
            InputStream inputStream = new FileInputStream(file);
            return (inputStream != null ? inputStream : new ByteArrayInputStream(new byte[0]));
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {
            if (!isAvailable()) {
                throw new IllegalStateException("File has already been moved - cannot be transferred again");
            }

            dest = file;
        }

        /**
         * Determine whether the multipart content is still available.
         * If a temporary file has been moved, the content is no longer available.
         */
        protected boolean isAvailable() {
            // Check whether current file size is different than original one.
            return (this.file.canRead());
        }

    }
