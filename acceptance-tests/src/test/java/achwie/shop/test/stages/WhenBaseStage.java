package achwie.shop.test.stages;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioStage;
import com.tngtech.jgiven.annotation.ScenarioState;

import achwie.shop.test.pages.CartPage;
import achwie.shop.test.pages.CatalogPage;
import achwie.shop.test.pages.LoginPage;
import achwie.shop.test.pages.OrderHistoryPage;
import achwie.shop.test.scenarios.TestUser;

/**
 * 
 * @author 12.12.2015, Achim Wiedemann
 */
public abstract class WhenBaseStage<SELF extends WhenBaseStage<SELF>> extends Stage<SELF> {
  @ScenarioState
  protected LoginPage loginPage;
  @ScenarioState
  protected CatalogPage catalogPage;
  @ScenarioState
  protected CartPage cartPage;
  @ScenarioState
  protected OrderHistoryPage orderHistoryPage;
  @ScenarioStage
  protected WhenOnCartPageStage cartStage;
  @ScenarioStage
  protected WhenOnCatalogPageStage catalogStage;

  public WhenOnCatalogPageStage user_logs_in_as(TestUser user) {
    if (!loginPage.isUserOnPage())
      loginPage.openPage();

    loginPage.loginWithCredentials(user.username, user.password);

    return catalogStage;
  }

  public WhenOnCartPageStage user_opens_cart_page() {
    cartPage.openPage();

    return cartStage;
  }

  public void user_opnes_order_history_page() {
    orderHistoryPage.openPage();
  }

  public WhenOnCatalogPageStage user_logs_out() {
    if (!catalogPage.isUserOnPage())
      catalogPage.openPage();

    catalogPage.clickLogOutIfAvailable();

    return catalogStage;
  }
}