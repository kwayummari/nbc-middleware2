package com.itrust.middlewares.nbc.modules.logging.filters;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class CachedBodyHttpServletResponse extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream cachedContent = new ByteArrayOutputStream();
    private final ServletOutputStream outputStream = new CachedServletOutputStream();
    private PrintWriter writer;

    public CachedBodyHttpServletResponse(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (writer == null) {
            writer = new PrintWriter(outputStream, true);
        }
        return writer;
    }

    public String getContent() {
        return cachedContent.toString();
    }

    private class CachedServletOutputStream extends ServletOutputStream {
        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            // Not implemented
        }

        @Override
        public void write(int b) throws IOException {
            cachedContent.write(b);
            CachedBodyHttpServletResponse.super.getOutputStream().write(b);
        }
    }
} 