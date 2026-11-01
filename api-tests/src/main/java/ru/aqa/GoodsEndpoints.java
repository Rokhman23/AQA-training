package ru.aqa;

/** Goods API paths are shared configuration, not embedded in endpoint methods. */
public final class GoodsEndpoints {
    private GoodsEndpoints() {
    }

    public static String list() { return TestConfig.value("api.goods.list.path"); }
    public static String add() { return TestConfig.value("api.goods.add.path"); }
    public static String byId() { return TestConfig.value("api.goods.by-id.path"); }
}
