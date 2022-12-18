package com.uz.shop.animal.world.services.pdf.generators;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.uz.shop.animal.world.services.pdf.ElementCreator;

import javax.servlet.http.HttpServletResponse;

public abstract class BaseGenerator {
    protected Document document;
    protected ElementCreator creator;

    public BaseGenerator() {
        this.document = new Document(this.getPageSize());
        this.creator = new ElementCreator(this.getFont());
    }

    protected abstract Rectangle getPageSize();
    protected abstract Font getFont();
    public abstract void generate(HttpServletResponse response);
}
