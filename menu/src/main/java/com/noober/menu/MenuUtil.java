package com.noober.menu;

import android.view.View;

import com.noober.menu.group.GroupedFloatMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Administrator
 * @date: 2022/2/2
 * @desc: //todo
 */
public class MenuUtil {

    public static <T> void showMenu(View targetView, List<IMenu<T>> menus, T info) {
        // 按 groupName 分组
        Map<String, List<IMenu<T>>> groupedMenus = new HashMap<>();
        for (IMenu<T> menu : menus) {
            String groupName = menu.groupName(); // 假设 IMenu 接口新增了 groupName 方法
            if (!groupedMenus.containsKey(groupName)) {
                groupedMenus.put(groupName, new ArrayList<>());
            }
            groupedMenus.get(groupName).add(menu);
        }

        // 构造 GroupedFloatMenu 所需的数据结构
        List<GroupedFloatMenu.MenuGroup> menuGroups = new ArrayList<>();
        for (Map.Entry<String, List<IMenu<T>>> entry : groupedMenus.entrySet()) {
            String groupName = entry.getKey();
            List<GroupedFloatMenu.MenuItem> menuItems = new ArrayList<>();
            for (IMenu<T> menu : entry.getValue()) {
                menuItems.add(new GroupedFloatMenu.MenuItem(menu.text(), v -> menu.onMenuClicked(-1, info)));
            }
            menuGroups.add(new GroupedFloatMenu.MenuGroup(groupName, menuItems));
        }

        // 显示分组菜单
        GroupedFloatMenu groupedFloatMenu = new GroupedFloatMenu(targetView.getContext(), menuGroups);
        groupedFloatMenu.showAsDropDown(targetView);
    }





    public interface IMenu<T>{
        String text();
       default String groupName() {
           return "";
       }
        void  onMenuClicked(int position,T bean);
    }
}
