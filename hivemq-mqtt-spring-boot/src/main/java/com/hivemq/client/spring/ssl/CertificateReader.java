/*
 * Copyright (c) 2024-present HiveMQ and the HiveMQ Community
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either expres or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 */

package com.hivemq.client.spring.ssl;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.security.Security;
import java.security.cert.Certificate;

/**
 * Reader for X.509 certificates.
 *
 * @author Sven Kobow
 * @since 1.0.0
 */
public final class CertificateReader {

    private static final Logger LOG = LoggerFactory.getLogger(CertificateReader.class);

    private CertificateReader() {
    }

    public static Certificate readCertificate(final File crtFile) {
        try {
            Security.addProvider(new BouncyCastleProvider());

            final JcaX509CertificateConverter certificateConverter = new JcaX509CertificateConverter().setProvider("BC");

            try (PEMParser parser = new PEMParser(new FileReader(crtFile))) {
                final X509CertificateHolder certHolder = (X509CertificateHolder) parser.readObject();

                return certificateConverter.getCertificate(certHolder);
            }
        } catch (Exception e) {
            LOG.error("Error reading certificate", e);
        }

        return null;
    }
}
