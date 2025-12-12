package guru.qa.rococo.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.component.CreatePaintingComponent;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class PaintingPage extends BasePage<PaintingPage> {

    private final SelenideElement editBtn = $("[data-testid=edit-painting]");
    private final SelenideElement description = $("#description");
    private final SelenideElement paintingTitle = $("header.text-center");
    private final SelenideElement paintingImg = $("#painting-image");
    private final SelenideElement artistName = $("#artistName");

    @Override
    public PaintingPage checkThatPageLoaded() {
        editBtn.shouldBe(visible);
        description.shouldBe(visible);
        paintingTitle.shouldBe(visible);
        artistName.shouldBe(visible);
        paintingImg.shouldBe(visible);
        return this;
    }

    @Step("Открытие редактирования картины")
    public CreatePaintingComponent editPainting() {
        editBtn.click();
        return new CreatePaintingComponent();
    }

    @Step("Проверка описания '{expectedText}'")
    public PaintingPage checkDescription(String expectedText) {
        description.shouldHave(text(expectedText));
        return this;
    }

    @Step("Проверка названия картины '{expectedText}'")
    public PaintingPage checkPaintingTitle(String expectedText) {
        paintingTitle.shouldHave(text(expectedText));
        return this;
    }

    @Step("Проверка изображения картины")
    public PaintingPage checkPaintingImg(String expectedBase64Image) {
        paintingImg.shouldHave(attribute("src", expectedBase64Image));
        return this;
    }

    @Step("Проверка имени художника '{expectedText}'")
    public PaintingPage checkArtistName(String expectedText) {
        artistName.shouldHave(text(expectedText));
        return this;
    }
}
