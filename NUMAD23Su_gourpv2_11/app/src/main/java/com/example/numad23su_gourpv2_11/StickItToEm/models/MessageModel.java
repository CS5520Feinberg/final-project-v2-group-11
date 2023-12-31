package com.example.numad23su_gourpv2_11.StickItToEm.models;

public class MessageModel {

    private String sender;
    private String receiver;
    private String sticker;

    private byte[] stickerImg;

    private long timestamp;

    public MessageModel() {}

    public MessageModel(String sender, String receiver, String sticker, long timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.sticker = sticker;
        this.timestamp = timestamp;
    }

    public String getReceiver() { return receiver; }
    public String getSender() { return sender; }
    public String getSticker() { return sticker; }
    public long getTimestamp() { return timestamp; }
    @Override
    public String toString(){
        return String.format("Sender: |%s|, Receiver: |%s|, sticker: |%s|, Timestamp: |%o|", this.getSender(), this.getReceiver(), this.getSticker(), this.getTimestamp());
    }
}
