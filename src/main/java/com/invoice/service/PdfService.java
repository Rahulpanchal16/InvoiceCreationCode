package com.invoice.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.invoice.model.Product;
import com.invoice.repo.ProductRepo;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.DottedBorder;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

@Service
public class PdfService {

	@Autowired
	private ProductRepo prodRepo;

	Double totalBillAmount = 0.0;

	public void createPdf() throws IOException {

		PdfWriter writer = new PdfWriter("E:\\PDF\\invoice.pdf");
		PdfDocument document = new PdfDocument(writer);
		document.setDefaultPageSize(PageSize.A4);
		Document pdf = new Document(document);

		String watermarkPath = "C:\\Users\\Rahul\\Downloads\\watermarkImg.png";
		ImageData watermarkImg = ImageDataFactory.create(watermarkPath);
		Image watermark = new Image(watermarkImg);
		float x = document.getDefaultPageSize().getWidth();
		float y = document.getDefaultPageSize().getHeight();
		watermark.setFixedPosition(x, y);
		watermark.setOpacity(0.1f);
		pdf.add(watermark);

//		pdf.setFont(PdfFontFactory.createFont(FontConstants.HELVETICA));
		float colThird = 190f;
		float col1 = 285f;
		float col2 = col1 + 150f;
		float totalColWidth[] = { col2, col1 };
		float[] fullWidth = { colThird * 3 };

		Table table = new Table(totalColWidth);
		table.addCell(new Cell().add("Order Invoice").setBorder(Border.NO_BORDER).setBold().setFontSize(20f));
		Table nestedTable = new Table(new float[] { col1 / 2, col1 / 2 });

		Random random = new Random();
		int randomNo = random.nextInt(900000) + 100000;
		String invoiceNo = "RP" + randomNo;

		nestedTable.addCell(getHeaderTextCell("Invoice no:"));
		nestedTable.addCell(getHeaderTextCellvalue(invoiceNo));
		nestedTable.addCell(getHeaderTextCell("Invoice date:"));
		LocalDateTime dateTime = LocalDateTime.now();
		int day = dateTime.getDayOfMonth();
		int month = dateTime.getMonthValue();
		int year = dateTime.getYear();
		String finalDate = day + "-" + month + "-" + year;
		nestedTable.addCell(getHeaderTextCellvalue(finalDate));

		table.addCell(new Cell().add(nestedTable).setBorder(Border.NO_BORDER));
		Border gb = new SolidBorder(Color.GRAY, 2f);
		Table divider = new Table(fullWidth);
		divider.setBorder(gb).setMarginBottom(15f);

		pdf.add(table);
//		pdf.add(newBlankLine);
		pdf.add(divider);

		float oneCol = 285f;
		float twoCol = 285f;
		float twoColWidth[] = { oneCol, twoCol };
		Table twoColumnTable = new Table(twoColWidth);
		twoColumnTable.addCell(getBillingAndShippingInfo("Billing Information"));
		twoColumnTable.addCell(getBillingAndShippingInfo("Shipping Information"));
		pdf.add(twoColumnTable.setMarginBottom(10f));

		Table twoColTable2 = new Table(twoColWidth);
		twoColTable2.addCell(getCell10Left("Company", true));
		twoColTable2.addCell(getCell10Left("Name", true));
		twoColTable2.addCell(getCell10Left("Shree Krishna steel product pvt ltd.", false));
		twoColTable2.addCell(getCell10Left("Rekha Panchal", false));
		pdf.add(twoColTable2);

		Table twoColTable3 = new Table(twoColWidth);
		twoColTable3.addCell(getCell10Left("Name", true));
		twoColTable3.addCell(getCell10Left("Address", true));
		twoColTable3.addCell(getCell10Left("Rahul Panchal", false));
		twoColTable3.addCell(getCell10Left("Flat no - 02, Gokul Garden, Katraj, Pune", false));
		pdf.add(twoColTable3);

		Table twoColTable4 = new Table(twoColWidth);
		twoColTable4.addCell(getCell10Left("Contact: ", true));
		twoColTable4.addCell(getCell10Left("Contact: ", true));
		twoColTable4.addCell(getCell10Left("1234567", false));
		twoColTable4.addCell(getCell10Left("8765432", false));
		pdf.add(twoColTable4);

		Table fullWidthTable = new Table(fullWidth);
		fullWidthTable.addCell(getCell10Left("Address", true));
		fullWidthTable.addCell(getCell10Left("Flat no - 02, Gokul Garden, Baner, Pune", false));
		fullWidthTable.addCell(getCell10Left("Email: " + "panchalrahul180@gmail.com", false));
		pdf.add(fullWidthTable.setMarginBottom(10f));

		Table dashedDivider = new Table(fullWidth);
		Border dashedBorder = new DottedBorder(Color.BLACK, 1f);
		dashedDivider.setBorder(dashedBorder);
		pdf.add(dashedDivider.setMarginBottom(5f));

		Paragraph prod = new Paragraph();
		prod.add("Products").setFontSize(13f).setTextAlignment(TextAlignment.LEFT).setBold();
		pdf.add(prod);

		float threeColWidth[] = { colThird, colThird, colThird };
		Table threeColTable = new Table(threeColWidth);
		threeColTable.addCell(getHeaderTextCell("Description").setFontColor(Color.WHITE)
				.setBackgroundColor(Color.DARK_GRAY).setTextAlignment(TextAlignment.LEFT));
		threeColTable.addCell(getHeaderTextCell("Quantity").setFontColor(Color.WHITE)
				.setBackgroundColor(Color.DARK_GRAY).setTextAlignment(TextAlignment.LEFT));
		threeColTable.addCell(getHeaderTextCell("Cost").setFontColor(Color.WHITE).setBackgroundColor(Color.DARK_GRAY)
				.setTextAlignment(TextAlignment.LEFT));

		List<Product> allProducts = this.prodRepo.findAll();

		allProducts.stream().forEach(product -> {
			threeColTable.addCell(getHeaderTextCellvalue(product.getName()));
			threeColTable.addCell(getHeaderTextCellvalue(Integer.toString(product.getQuantity())));
			threeColTable.addCell(getHeaderTextCellvalue(Double.toString(product.getAmount())));
			Double singleProductTotalAmount = product.getAmount() * product.getQuantity();
			totalBillAmount += singleProductTotalAmount;
		});
		pdf.add(threeColTable);

		pdf.add(dashedDivider);

		Table totalBillTable = new Table(threeColWidth);
		totalBillTable.addCell(getHeaderTextCellvalue(""));
		totalBillTable.addCell(getBillingAndShippingInfo("Total"));
		totalBillTable.addCell(getHeaderTextCellvalue("Rs." + Double.toString(totalBillAmount)));
		pdf.add(totalBillTable.setBorder(Border.NO_BORDER));

		pdf.add(dashedDivider);

		pdf.add(divider.setMarginTop(5f));

		Paragraph terms = new Paragraph();
		terms.add("Terms & Conditions").setBold().setFontSize(13f).setTextAlignment(TextAlignment.LEFT);
		pdf.add(terms);

		Paragraph tnc = new Paragraph();
		tnc.add("1. All sales are final. No refunds or exchanges unless otherwise stated in writing.\n")
				.setFontSize(8f);
		tnc.add("2. All charges listed on this bill are subject to applicable taxes and fees.\n").setFontSize(8f);
		tnc.add("3. Any disputes regarding charges on this bill must be brought to our attention within 30 days of receipt.")
				.setFontSize(8f);
		pdf.add(tnc);

		pdf.close();
	}

	static Cell getHeaderTextCell(String value) {
		return new Cell().add(value).setBorder(Border.NO_BORDER).setBold().setTextAlignment(TextAlignment.RIGHT)
				.setFontSize(10f);
	}

	static Cell getHeaderTextCellvalue(String value) {
		return new Cell().add(value).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
	}

	static Cell getBillingAndShippingInfo(String value) {
		return new Cell().add(value).setFontSize(12f).setBold().setBorder(Border.NO_BORDER)
				.setTextAlignment(TextAlignment.LEFT);
	}

	static Cell getCell10Left(String value, boolean isBold) {
		Cell myCell = new Cell().add(value).setFontSize(10f).setBorder(Border.NO_BORDER)
				.setTextAlignment(TextAlignment.LEFT);
		return isBold ? myCell.setBold() : myCell;
	}

}
