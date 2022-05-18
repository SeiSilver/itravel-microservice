package com.itravel.api.shop.constraint;

public class DefaultValue {
    public static final String PAGE = "1";
    public static final String SIZE = "10";
    public static final String SORT_TYPE_DESC = "desc";
    public static final Integer SUB_SERVICE_ID = 0;
    public static final String SUB_SERVICE_UPDATE = "update";
    public static final String SUB_SERVICE_DELETE = "delete";
    public static final String SERVICE_IMAGE = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAgAAAAIACAYAAAD0eNT6AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAACLESURBVHgB7d07sB3lmS7gBgG6YE4kBehUIUeiIJuSUmsggyrjwCRjxpwMidDIRDZ1jKuACQ7GdjZACMZOmMDYZSYC49BSKg5EFgEEUmSQhEASs9611LDRdV9W9+ru73mq9ggYs5H26v7/9//+2y2P/ctnXzUAQCm3NgBAOQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQQIAABQkAABAQbc1sEW7997SPHr4jqYLZz/7qnnz5S+bs59+1QCwPAIAW7Zv/7bm0A+6e5T+8cGl5m9vXWgAWB5TAGzZnXc1AIyMAAAABQkAAFCQAAAABQkAAFCQAAAABQkAAFCQAAAABQkAAFCQAAAABQkAAFCQAAAABQkAAFCQAAAABQkAAFCQAAAABQkAAFCQAAAABQkAAFCQAAAABQkAAFCQAAAABQkAAFCQAAAABQkAAFDQbQ3AJuzee0vz3XtvbXbffUuzZ++tza7v5Ndbml133dLcOfvaeVcz//VGTn/8VXPm06+as7OvU7O/PvXJpeajDy/N/9nJDxb/HOiGAADcVDr7+w9sa/bNOvz7Zr/m72/Wua/3++5uFt/nvmv8/09+cGkWEi41J45fbN6ffSUUAMshAABXySj+4APb5p39gQe3LaWz34wEjnwdeHDRVKVK8P6xi83xv15oThy7pEIAWyAAAHPp9A89sm3W8d/W3HdwWzNEmWLY84PbmkM/WDRd7/3xwjwMHHvnYgNsjAAAxd138NbmoR/dPu/0VzXS36xDl8NAKgP//cYXzd9nQeD0J6oCsB4CQAcyktqzd1a+3L+YM83iqPzaNq6Z91wrC6Hi1MeLkuapT76a//XJDxd/b95zudYuXpt/Vnd/sxkmi9Di9OUFaVP++R965Lb511BH+xuRysCPn94++1pUBd58+YvRBIHHn76jeeix25suvP3Gl81rL37RLNvaNi7v09p3qHX2s28WeGrLhkkAWJKMolI63bf/1g03qG0g2L332v9eXqKPPvhmIdT7xy41rF8+myxgy3z2PWuC2EZkMVq+8vM/cWzco8x0+j88cse805yitiowliDQrm/o5HvP2qRlBIB0+PcfXCwATTu3ewvPzpTepbETALag7fS/N2tQuyyd5nsnVLTBYu1CKHOf15YG6+HHMsK9fUuNVatdjNbOPefn/95bF0bVgOVZ/fHT0+34r5TP6uCD2+aj4Ddf/rJh47qYHrryXWrDwLF3Lxjc9EwA2KB2dfQqS6drF0K1YWBMJc8upbN/dDa6zefTpbWBbOgjzTTijx6+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";
    public static final String NOTIFY_SHOP_STATUS_CHANGE_TITLE = "Your shop's status had been change";
    public static final String NOTIFY_SHOP_STATUS_CHANGE_MESSAGE = "Shop %s status had been change to %s by moderator/admin %s";
    public static final String NOTIFY_SERVICE_STATUS_CHANGE_TITLE = "Your service's status had been change";
    public static final String NOTIFY_SERVICE_STATUS_CHANGE_MESSAGE = "Service %s status had been change to %s by moderator/admin %s";

}
