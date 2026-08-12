package com.srots.presentation.navigation.registry;

import com.srots.presentation.navigation.model.NavigationGroup;
import com.srots.presentation.navigation.model.NavigationItem;
import com.srots.presentation.navigation.model.NavigationRouteId;
import java.util.ArrayList;
import java.util.List;

/**
 * Default SROTS sidebar catalog from Prompt 08 §8 / §57 / §58.
 */
public final class DefaultNavigationCatalog {

    private DefaultNavigationCatalog() {
    }

    public static List<NavigationItem> createDefaultItems() {
        List<NavigationItem> items = new ArrayList<>();

        // Overview
        items.add(item("Overview", NavigationRouteId.OVERVIEW, null, NavigationGroup.OVERVIEW, 10, "overview"));
        items.add(item("Design System", NavigationRouteId.DESIGN_SYSTEM, null, NavigationGroup.SYSTEM, 15, "design"));

        // My Workspace
        items.add(item("My Workspace", NavigationRouteId.WORKSPACE, null, NavigationGroup.WORKSPACE, 20, "workspace"));
        items.add(item("My Tasks", NavigationRouteId.WORKSPACE_TASKS, NavigationRouteId.WORKSPACE, NavigationGroup.WORKSPACE, 21, "tasks"));
        items.add(item("My Projects", NavigationRouteId.WORKSPACE_PROJECTS, NavigationRouteId.WORKSPACE, NavigationGroup.WORKSPACE, 22, "projects"));
        items.add(item("My Issues", NavigationRouteId.WORKSPACE_ISSUES, NavigationRouteId.WORKSPACE, NavigationGroup.WORKSPACE, 23, "issues"));
        items.add(item("My Activity", NavigationRouteId.WORKSPACE_ACTIVITY, NavigationRouteId.WORKSPACE, NavigationGroup.WORKSPACE, 24, "activity"));

        // Company
        items.add(item("Company", NavigationRouteId.COMPANY, null, NavigationGroup.COMPANY, 30, "company"));
        items.add(item("Employees", NavigationRouteId.COMPANY_EMPLOYEES, NavigationRouteId.COMPANY, NavigationGroup.COMPANY, 31, "users"));
        items.add(item("Teams", NavigationRouteId.COMPANY_TEAMS, NavigationRouteId.COMPANY, NavigationGroup.COMPANY, 32, "teams"));
        items.add(item("Departments", NavigationRouteId.COMPANY_DEPARTMENTS, NavigationRouteId.COMPANY, NavigationGroup.COMPANY, 33, "departments"));
        items.add(item("Organization", NavigationRouteId.COMPANY_ORGANIZATION, NavigationRouteId.COMPANY, NavigationGroup.COMPANY, 34, "organization"));

        // Work
        items.add(item("Projects", NavigationRouteId.PROJECTS, null, NavigationGroup.WORK, 40, "projects"));
        items.add(item("Tasks", NavigationRouteId.TASKS, null, NavigationGroup.WORK, 41, "tasks"));
        items.add(item("Issues", NavigationRouteId.ISSUES, null, NavigationGroup.WORK, 42, "issues"));

        // Products
        items.add(item("Products", NavigationRouteId.PRODUCTS, null, NavigationGroup.PRODUCTS, 50, "products"));

        items.add(item("SROTS", NavigationRouteId.SROTS_PRODUCT, NavigationRouteId.PRODUCTS, NavigationGroup.PRODUCTS, 51, "srots"));
        items.add(item("Overview", NavigationRouteId.SROTS_OVERVIEW, NavigationRouteId.SROTS_PRODUCT, NavigationGroup.PRODUCTS, 52, "overview"));
        items.add(item("Engineering", NavigationRouteId.SROTS_ENGINEERING, NavigationRouteId.SROTS_PRODUCT, NavigationGroup.PRODUCTS, 53, "engineering"));
        items.add(item("Versions", NavigationRouteId.SROTS_VERSIONS, NavigationRouteId.SROTS_PRODUCT, NavigationGroup.PRODUCTS, 54, "versions"));
        items.add(item("Releases", NavigationRouteId.SROTS_RELEASES, NavigationRouteId.SROTS_PRODUCT, NavigationGroup.PRODUCTS, 55, "releases"));

        items.add(item("COMPTY", NavigationRouteId.COMPTY, NavigationRouteId.PRODUCTS, NavigationGroup.PRODUCTS, 60, "compty"));
        items.add(item("Overview", NavigationRouteId.COMPTY_OVERVIEW, NavigationRouteId.COMPTY, NavigationGroup.PRODUCTS, 61, "overview"));
        items.add(item("Requirements", NavigationRouteId.COMPTY_REQUIREMENTS, NavigationRouteId.COMPTY, NavigationGroup.PRODUCTS, 62, "requirements"));
        items.add(item("Engineering", NavigationRouteId.COMPTY_ENGINEERING, NavigationRouteId.COMPTY, NavigationGroup.PRODUCTS, 63, "engineering"));
        items.add(item("Versions", NavigationRouteId.COMPTY_VERSIONS, NavigationRouteId.COMPTY, NavigationGroup.PRODUCTS, 64, "versions"));
        items.add(item("Releases", NavigationRouteId.COMPTY_RELEASES, NavigationRouteId.COMPTY, NavigationGroup.PRODUCTS, 65, "releases"));
        items.add(item("Analytics", NavigationRouteId.COMPTY_ANALYTICS, NavigationRouteId.COMPTY, NavigationGroup.PRODUCTS, 66, "analytics"));

        // Engineering
        items.add(item("Engineering", NavigationRouteId.ENGINEERING, null, NavigationGroup.ENGINEERING, 70, "engineering"));
        items.add(item("Requirements", NavigationRouteId.ENGINEERING_REQUIREMENTS, NavigationRouteId.ENGINEERING, NavigationGroup.ENGINEERING, 71, "requirements"));
        items.add(item("Development", NavigationRouteId.ENGINEERING_DEVELOPMENT, NavigationRouteId.ENGINEERING, NavigationGroup.ENGINEERING, 72, "development"));
        items.add(item("Builds", NavigationRouteId.ENGINEERING_BUILDS, NavigationRouteId.ENGINEERING, NavigationGroup.ENGINEERING, 73, "builds"));
        items.add(item("Technical Issues", NavigationRouteId.ENGINEERING_ISSUES, NavigationRouteId.ENGINEERING, NavigationGroup.ENGINEERING, 74, "issues"));

        // Release
        items.add(item("Versions", NavigationRouteId.VERSIONS, null, NavigationGroup.RELEASE, 80, "versions"));
        items.add(item("Releases", NavigationRouteId.RELEASES, null, NavigationGroup.RELEASE, 81, "releases"));
        items.add(item("Deployments", NavigationRouteId.DEPLOYMENTS, null, NavigationGroup.RELEASE, 82, "deployments"));

        // Business / Sales
        items.add(item("Sales", NavigationRouteId.SALES, null, NavigationGroup.BUSINESS, 90, "sales"));
        items.add(item("Leads", NavigationRouteId.SALES_LEADS, NavigationRouteId.SALES, NavigationGroup.BUSINESS, 91, "leads"));
        items.add(item("Opportunities", NavigationRouteId.SALES_OPPORTUNITIES, NavigationRouteId.SALES, NavigationGroup.BUSINESS, 92, "opportunities"));
        items.add(item("Customers", NavigationRouteId.SALES_CUSTOMERS, NavigationRouteId.SALES, NavigationGroup.BUSINESS, 93, "customers"));
        items.add(item("Deals", NavigationRouteId.SALES_DEALS, NavigationRouteId.SALES, NavigationGroup.BUSINESS, 94, "deals"));

        // Support / Knowledge / Analytics / Settings
        items.add(item("Support", NavigationRouteId.SUPPORT, null, NavigationGroup.SUPPORT, 100, "support"));
        items.add(item("Knowledge", NavigationRouteId.KNOWLEDGE, null, NavigationGroup.SUPPORT, 110, "knowledge"));
        items.add(NavigationItem.builder()
                .title("Analytics")
                .route(NavigationRouteId.ANALYTICS)
                .group(NavigationGroup.SYSTEM)
                .order(120)
                .iconKey("analytics")
                .role("ADMIN")
                .build());
        items.add(NavigationItem.builder()
                .title("Settings")
                .route(NavigationRouteId.SETTINGS)
                .group(NavigationGroup.SYSTEM)
                .order(130)
                .iconKey("settings")
                .build());

        return List.copyOf(items);
    }

    private static NavigationItem item(
            String title,
            NavigationRouteId route,
            NavigationRouteId parent,
            NavigationGroup group,
            int order,
            String iconKey) {
        NavigationItem.Builder builder = NavigationItem.builder()
                .title(title)
                .route(route)
                .group(group)
                .order(order)
                .iconKey(iconKey);
        if (parent != null) {
            builder.parent(parent);
        }
        return builder.build();
    }
}
