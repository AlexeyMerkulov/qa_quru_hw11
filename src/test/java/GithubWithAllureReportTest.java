import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byLinkText;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

public class GithubWithAllureReportTest {

    private static final String REPO = "eroshenkoam/allure-example";
    private static final String ISSUE_NAME = "issue_to_test_allure_report";

    @Test
    void verifyIssueExistsBySelenideOnly() {
        SelenideLogger.addListener("allure", new AllureSelenide());

        open("https://github.com/");

        $(".header-search-button").click();
        $("#query-builder-test").setValue(REPO).pressEnter();
        $(byLinkText(REPO)).click();
        $("#issues-tab").click();

        $(byText(ISSUE_NAME)).shouldBe(visible);
    }

    @Test
    void verifyIssueExistsByLambdaSteps() {
        SelenideLogger.addListener("allure", new AllureSelenide());

        step("Открываем главную страницу", () -> open("https://github.com"));
        step("Ищем репозиторий " + REPO, () -> {
            $(".header-search-button").click();
            $("#query-builder-test").setValue(REPO).pressEnter();
        });
        step("Кликаем по ссылке репозитория " + REPO, () -> $(byLinkText(REPO)).click());
        step("Открываем таб Issues", () -> $("#issues-tab").click());
        step("Проверяем наличие Issue с именем " + ISSUE_NAME, () -> $(byText(ISSUE_NAME)).shouldBe(visible));
    }

    @Test
    void verifyIssueExistsByAnnotationSteps() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        WebSteps steps = new WebSteps();

        steps.openMainPage();
        steps.searchForRepository(REPO);
        steps.clickOnRepositoryLink(REPO);
        steps.openIssuesTab();
        steps.verifyIssueExists(ISSUE_NAME);
    }
}
