package com.uz.shop.animal.world.services.pdf;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.uz.shop.animal.world.services.pdf.fake_objects.Customer;
import com.uz.shop.animal.world.utils.Dictionary;

public class ElementCreator {
    private Font fontNormal;
    private Font fontBold;

    public ElementCreator(Font font) {
        this.fontNormal = font;
        this.fontBold = new Font(font);
        this.fontBold.setStyle(Font.BOLD);
    }

    public PdfPCell getBlankCell() {
        PdfPCell cell = new PdfPCell(new Paragraph(" "));
        cell.setBorder(0);

        return cell;
    }

    public Paragraph getParagraph(String content) {
        return this.getParagraph(content, false);
    }
    public Paragraph getParagraph(String content, boolean isBold) {
        return new Paragraph(content, isBold ? fontBold : fontNormal);
    }

    public PdfPCell getCell(String content) {
        return this.getCell(content, new int[] {0, 0, 0, 0});
    }
    public PdfPCell getCell(String content, int horizontalAlign) {
        return this.getCell(content, horizontalAlign, Element.ALIGN_MIDDLE);
    }
    public PdfPCell getCell(String content, int horizontalAlign, int verticalAlign) {
        return this.getCell(content, new int[] {0, 0, 0, 0}, horizontalAlign, verticalAlign);
    }
    public PdfPCell getCell(String content, int[] border) {
        return this.getCell(content, border, Element.ALIGN_LEFT);
    }
    public PdfPCell getCell(String content, int[] border, int horizontalAlign) {
        return this.getCell(content, border, horizontalAlign, Element.ALIGN_MIDDLE);
    }
    public PdfPCell getCell(String content, int[] border, int horizontalAlign, int verticalAlign) {
        return this.getCell(this.getParagraph(content), border, horizontalAlign, verticalAlign);
    }
    public PdfPCell getCell(int[] border) {
        return this.getCell((Element) null, border);
    }
    public PdfPCell getCell(Element element, int[] border) {
        return this.getCell(element, border, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE);
    }
    public PdfPCell getCell(Element element, int[] border, int horizontalAlign) {
        return this.getCell(element, border, horizontalAlign, Element.ALIGN_MIDDLE);
    }
    /**
     @param border - values for thickness of border: [top right bottom left]
     @param horizontalAlign - value of Element const
     @param verticalAlign - value of Element const
     */
    public PdfPCell getCell(Element element, int[] border, int horizontalAlign, int verticalAlign) {
        PdfPCell cell;
        if (element instanceof Image) {
            cell = new PdfPCell((Image) element, true);
        } else if (element instanceof Paragraph) {
            cell = new PdfPCell((Paragraph) element);
        } else {
            cell = new PdfPCell();
            if (element != null) {
                cell.addElement(element);
            }
        }

        cell.setHorizontalAlignment(horizontalAlign);
        cell.setVerticalAlignment(verticalAlign);
        cell.setPaddingBottom(5);

        cell.setBorder(0);
        if (border != null && border.length == 4) {
            cell.setBorderWidthTop(border[0]);
            cell.setBorderWidthRight(border[1]);
            cell.setBorderWidthBottom(border[2]);
            cell.setBorderWidthLeft(border[3]);
        }

        return cell;
    }

    public PdfPCell getCompanyData(String label) {
        PdfPCell cell = new PdfPCell();
        cell.addElement(this.getParagraph(label, true));
        cell.addElement(this.getParagraph(Dictionary.Company.NAME));
        cell.addElement(this.getParagraph(Dictionary.Company.STREET));
        cell.addElement(this.getParagraph(Dictionary.Company.CITY));
        cell.addElement(this.getParagraph(Dictionary.Company.COUNTRY));

        cell.setBorder(0);
        return cell;
    }

    // TODO: PDF - zmienić obiekt customer na prawillny
    public PdfPCell getCustomerData(String label, Customer customer) {
        PdfPCell cell = new PdfPCell();
        cell.addElement(this.getParagraph(label, true));
        cell.addElement(this.getParagraph(customer.name + " " + customer.surname));
        cell.addElement(this.getParagraph(customer.street));
        cell.addElement(this.getParagraph(customer.city));
        cell.addElement(this.getParagraph(customer.country));

        cell.setBorder(0);
        return cell;
    }

}
