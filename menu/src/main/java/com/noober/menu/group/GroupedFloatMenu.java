package com.noober.menu.group;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupedFloatMenu extends PopupWindow {

    private Context context;
    private List<MenuGroup> menuGroups;

    public GroupedFloatMenu(Context context, List<MenuGroup> menuGroups) {
        super(context);
        this.context = context;
        this.menuGroups = menuGroups;
        init();
    }

    private void init() {
        LinearLayout mainLayout = new LinearLayout(context);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(20, 20, 20, 20);

        for (MenuGroup group : menuGroups) {
            // 添加组名
            TextView groupNameView = new TextView(context);
            groupNameView.setText(group.getGroupName());
            groupNameView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            groupNameView.setTextColor(Color.BLACK);
            groupNameView.setPadding(0, 10, 0, 10);
            mainLayout.addView(groupNameView);

            // 添加 GridLayout
            GridLayout gridLayout = new GridLayout(context);
            gridLayout.setColumnCount(3); // 每行显示3个菜单项
            gridLayout.setRowCount((int) Math.ceil(group.getItems().size() / 3.0));

            for (MenuItem menuItem : group.getItems()) {
                TextView itemTextView = new TextView(context);
                itemTextView.setText(menuItem.getItem());
                itemTextView.setGravity(Gravity.CENTER);
                itemTextView.setPadding(10, 10, 10, 10);
                itemTextView.setBackgroundResource(android.R.drawable.btn_default);
                itemTextView.setOnClickListener(menuItem.getOnClickListener());

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                params.setMargins(5, 5, 5, 5);
                itemTextView.setLayoutParams(params);

                gridLayout.addView(itemTextView);
            }

            mainLayout.addView(gridLayout);
        }

        setContentView(mainLayout);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setBackgroundDrawable(null);
    }

    public static class MenuGroup {
        private String groupName;
        private List<MenuItem> items;

        public MenuGroup(String groupName, List<MenuItem> items) {
            this.groupName = groupName;
            this.items = items;
        }

        public String getGroupName() {
            return groupName;
        }

        public List<MenuItem> getItems() {
            return items;
        }
    }

    public static class MenuItem {
        private String item;
        private View.OnClickListener onClickListener;

        public MenuItem(String item, View.OnClickListener onClickListener) {
            this.item = item;
            this.onClickListener = onClickListener;
        }

        public String getItem() {
            return item;
        }

        public View.OnClickListener getOnClickListener() {
            return onClickListener;
        }
    }
}