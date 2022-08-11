package de.iv.jda.core;

import net.dv8tion.jda.api.entities.Member;

import java.util.HashMap;

public class RuntimeData {

    private Member owner;
    private final HashMap<String, Object> dataContainer = new HashMap<>();

    public RuntimeData(Member owner) {
        this.owner = owner;
    }

    public Member getOwner() {
        return owner;
    }

    public void setOwner(Member owner) {
        this.owner = owner;
    }

    public void setData(String key, Object data) {
        this.dataContainer.put(key, data);
    }

    public Object getData(String key) {
        return dataContainer.get(key);
    }

}
