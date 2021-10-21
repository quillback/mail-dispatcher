package com.coris.example.maildispatcher.network.outbound;

import lombok.Builder;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

@Getter
public class MailPart {
    private final String from;
    private final String[] to;
    private final String[] cc;
    private final String[] bcc;
    private final String subject;
    private final String body;
    private final LocalDateTime when;
    private final Map<Path, String> pathWithMimeType;

    @Builder
    public MailPart(@NotNull String from, @NotNull String[] to, String[] cc, String[] bcc, String subject, String body) {
        this.from           = from;
        this.to             = to;
        this.cc             = cc;
        this.bcc            = bcc;
        this.subject        = subject;
        this.body           = body;
        this.when           = LocalDateTime.now();
        pathWithMimeType    = new WeakHashMap<>();

        if (Strings.isEmpty(to[0]))
            throw new IllegalArgumentException("Must have at least one recipient");

        if (Strings.isEmpty(from))
            throw new IllegalArgumentException("Must have the address of the sender");
    }

    public boolean hasAttachment(){
        return !this.pathWithMimeType.isEmpty();
    }

    public MailPart addExcelFile(@NotNull final Path path){
        return this.addAttachment(path, "application/vnd.ms-excel");
    }

    @SuppressWarnings("SameParameterValue")
    private MailPart addAttachment(@NotNull final Path path, @NotNull final String mimeType){
        this.pathWithMimeType.put(path, mimeType);
        return this;
    }

    public Map<Path, String> getPathWithMimeType() {
        return Collections.unmodifiableMap(pathWithMimeType);
    }
}
