package com.example.numad23su_gourpv2_11.StickItToEm.models;

public class Message {

    private String sender;
    private String receiver;
    private String sticker;

    private byte[] stickerImg;

    private long timestamp;

    public Message() {}

    public Message(String sender, String receiver, String sticker, long timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.sticker = sticker;
        this.timestamp = timestamp;
    }

    public String getReceiver() { return receiver; }
    public String getSender() { return sender; }
    public String getSticker() { return sticker; }
    public byte[] getStickerImg() { return stickerImg; }
    public long getTimestamp() { return timestamp; }
}
