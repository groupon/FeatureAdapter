package com.groupon.android.featureadapter.sample.model;

import java.util.concurrent.Callable;
import rx.Single;

public class DealApiClient {

  public static Single<Deal> getDeal(long throttleInMs) {
    return getDeal("goods", throttleInMs);
  }

  public static Single<Deal> getDeal(String url, long throttleInMs) {
    return Single.fromCallable(
        new Callable<Deal>() {
          @Override
          public Deal call() throws Exception {
            if (throttleInMs > 0) {
              System.out.println("getDeal sleeping for: " + throttleInMs + "ms");
              Thread.sleep(throttleInMs);
            }

            Deal deal =
                Deal.builder()
                    .setId("example-deal")
                    .setTitle("Awesome Pants Deal")
                    .setPrice("$ 345")
                    .setImageUrl(
                        "https://img.grouponcdn.com/deal/jNuP1sbPNj2PyekX4Vexcgw8kqx/jN-1913x1148/v1/c700x420.jpg")
                    .addTrait(
                        Trait.builder()
                            .setIndex(0)
                            .setName("Size")
                            .addVariation(buildVariation(0, 0, "Small"))
                            .addVariation(buildVariation(0, 1, "Medium")))
                    .addTrait(
                        Trait.builder()
                            .setIndex(1)
                            .setName("Color")
                            .addVariation(buildVariation(1, 0, "Red"))
                            .addVariation(buildVariation(1, 1, "Blue"))
                            .addVariation(buildVariation(1, 2, "Green"))
                            .addVariation(buildVariation(1, 3, "Yellow"))
                            .addVariation(buildVariation(1, 4, "Black"))
                            .addVariation(buildVariation(1, 5, "White"))
                            .addVariation(buildVariation(1, 6, "Stripey")))
                    .addOption(
                        buildOption(
                            "12345",
                            "Awesome Pants Deal Option A",
                            "$ 134",
                            "https://img.grouponcdn.com/deal/2s6BJBudHSHQg4eu4d5iHMRUWi8u/2s-2048x1229/v1/c700x420.jpg"))
                    .addOption(
                        buildOption(
                            "123456",
                            "Awesome Pants Deal Option B",
                            "$ 56",
                            "https://img.grouponcdn.com/deal/jNuP1sbPNj2PyekX4Vexcgw8kqx/jN-1913x1148/v1/c700x420.jpg"))
                    .build();

            return deal;
          }
        });
  }

  private static Variation buildVariation(int traitIndex, int variantIndex, String variantName) {
    return Variation.builder()
        .setTraitIndex(traitIndex)
        .setIndex(variantIndex)
        .setValue(variantName)
        .build();
  }

  private static Option buildOption(
      String uuid, String optionTitle, String optionPrice, String url) {
    return Option.builder()
        .setUuid(uuid)
        .setTitle(optionTitle)
        .setHtml(getShortHtml())
        .setPrice(optionPrice)
        .setImageUrl(url)
        .build();
  }

  private static String getShortHtml() {
    return "Details<br><br><ul><li><a href=\"/global_conditions\">Ver condiciones</a> que aplican a todos los Groupones.</li></ul>";
  }

  private static String getLongHtml() {
    return "\"<br><p style=\"\"><span style=\"font-weight: bold;\">DC. Sed porttitor nisi non ligula tincidunt dictum</span></p><br><ul style=\"\"><li style=\"\">Vivamus nec orci nec mi dapibus euismod at vel nisl</li><li style=\"\">Etiam euismod quam quis ex pellentesque, vitae egestas lorem sollicitudin</li></ul><p style=\"\">Phasellus eu nulla metus. Nam vel lectus a augue rhoncus euismod. Proin orci sem, pellentesque ac ullamcorper non, commodo id libero. Maecenas pellentesque nisi a hendrerit molestie.</p><br><p style=\"\"><span style=\"font-weight: bold;\">Cras nec magna</span></p><br><p style=\"\">Morbi ex mi, dapibus tristique facilisis sed, pulvinar quis dui. Fusce in bibendum lectus. Vivamus in purus rhoncus, ultricies sem id, imperdiet elit. Integer ultricies euismod ante, eget molestie ex aliquam vel. Vivamus non dolor neque. Ut id pharetra leo, id molestie metus.";
  }
}
