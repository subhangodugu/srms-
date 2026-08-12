package com.srots.presentation.navigation.resolver;

import com.srots.presentation.navigation.model.NavigationRouteId;
import java.util.EnumMap;
import java.util.Map;

/**
 * Default titles and descriptions for major SROTS routes.
 */
public final class DefaultViewResolver implements ViewResolver {

    private final Map<NavigationRouteId, ViewDefinition> definitions;

    public DefaultViewResolver() {
        Map<NavigationRouteId, ViewDefinition> map = new EnumMap<>(NavigationRouteId.class);
        put(map, NavigationRouteId.OVERVIEW, "Overview", "Executive dashboard and platform summary.");
        put(map, NavigationRouteId.DESIGN_SYSTEM, "Design System", "SROTS visual system showcase for development and QA.");
        put(map, NavigationRouteId.LOGIN, "Sign in", "Authenticate to continue.");

        put(map, NavigationRouteId.WORKSPACE, "My Workspace", "Your personal tasks, projects, and activity.");
        put(map, NavigationRouteId.WORKSPACE_TASKS, "My Tasks", "Tasks assigned to you.");
        put(map, NavigationRouteId.WORKSPACE_PROJECTS, "My Projects", "Projects you are working on.");
        put(map, NavigationRouteId.WORKSPACE_ISSUES, "My Issues", "Issues assigned to you.");
        put(map, NavigationRouteId.WORKSPACE_ACTIVITY, "My Activity", "Recent personal activity.");

        put(map, NavigationRouteId.COMPANY, "Company", "Organization structure and people.");
        put(map, NavigationRouteId.COMPANY_EMPLOYEES, "Employees", "Manage company employees.");
        put(map, NavigationRouteId.COMPANY_TEAMS, "Teams", "Manage teams and membership.");
        put(map, NavigationRouteId.COMPANY_DEPARTMENTS, "Departments", "Manage departments.");
        put(map, NavigationRouteId.COMPANY_ORGANIZATION, "Organization", "Organization chart and structure.");

        put(map, NavigationRouteId.PROJECTS, "Projects", "Company projects and delivery work.");
        put(map, NavigationRouteId.PROJECT_DETAILS, "Project Details", "Details for a selected project.");
        put(map, NavigationRouteId.TASKS, "Tasks", "Task backlog and assignments.");
        put(map, NavigationRouteId.TASK_DETAILS, "Task Details", "Details for a selected task.");
        put(map, NavigationRouteId.ISSUES, "Issues", "Issue tracking and triage.");
        put(map, NavigationRouteId.ISSUE_DETAILS, "Issue Details", "Details for a selected issue.");

        put(map, NavigationRouteId.PRODUCTS, "Products", "Products managed by SROTS.");
        put(map, NavigationRouteId.PRODUCT_DETAILS, "Product Details", "Details for a selected product.");
        put(map, NavigationRouteId.SROTS_PRODUCT, "SROTS", "SROTS platform product workspace.");
        put(map, NavigationRouteId.SROTS_OVERVIEW, "SROTS Overview", "SROTS product overview.");
        put(map, NavigationRouteId.SROTS_ENGINEERING, "SROTS Engineering", "SROTS engineering workspace.");
        put(map, NavigationRouteId.SROTS_VERSIONS, "SROTS Versions", "SROTS version catalog.");
        put(map, NavigationRouteId.SROTS_RELEASES, "SROTS Releases", "SROTS release board.");

        put(map, NavigationRouteId.COMPTY, "COMPTY", "COMPTY product control plane.");
        put(map, NavigationRouteId.COMPTY_OVERVIEW, "COMPTY Overview", "COMPTY product overview.");
        put(map, NavigationRouteId.COMPTY_REQUIREMENTS, "COMPTY Requirements", "COMPTY requirements backlog.");
        put(map, NavigationRouteId.COMPTY_ENGINEERING, "COMPTY Engineering", "COMPTY engineering workspace.");
        put(map, NavigationRouteId.COMPTY_VERSIONS, "COMPTY Versions", "COMPTY version catalog.");
        put(map, NavigationRouteId.COMPTY_RELEASES, "COMPTY Releases", "COMPTY release board.");
        put(map, NavigationRouteId.COMPTY_ANALYTICS, "COMPTY Analytics", "COMPTY operational analytics.");

        put(map, NavigationRouteId.ENGINEERING, "Engineering", "Cross-product engineering work.");
        put(map, NavigationRouteId.ENGINEERING_REQUIREMENTS, "Requirements", "Engineering requirements.");
        put(map, NavigationRouteId.ENGINEERING_DEVELOPMENT, "Development", "Development activity.");
        put(map, NavigationRouteId.ENGINEERING_BUILDS, "Builds", "Build pipelines and artifacts.");
        put(map, NavigationRouteId.ENGINEERING_ISSUES, "Technical Issues", "Engineering technical issues.");

        put(map, NavigationRouteId.VERSIONS, "Versions", "Product versions.");
        put(map, NavigationRouteId.RELEASES, "Releases", "Release board and gates.");
        put(map, NavigationRouteId.RELEASE_DETAILS, "Release Details", "Details for a selected release.");
        put(map, NavigationRouteId.DEPLOYMENTS, "Deployments", "Deployment history and status.");

        put(map, NavigationRouteId.SALES, "Sales", "Sales pipeline and customers.");
        put(map, NavigationRouteId.SALES_LEADS, "Leads", "Sales leads.");
        put(map, NavigationRouteId.SALES_OPPORTUNITIES, "Opportunities", "Sales opportunities.");
        put(map, NavigationRouteId.SALES_CUSTOMERS, "Customers", "Customer accounts.");
        put(map, NavigationRouteId.SALES_DEALS, "Deals", "Deal pipeline.");

        put(map, NavigationRouteId.SUPPORT, "Support", "Helpdesk, tickets, and SLAs.");
        put(map, NavigationRouteId.KNOWLEDGE, "Knowledge", "Docs, SOPs, and knowledge base.");
        put(map, NavigationRouteId.ANALYTICS, "Analytics", "Telemetry, utilization, and BI.");
        put(map, NavigationRouteId.SETTINGS, "Settings", "Governance, RBAC, and system configuration.");
        put(map, NavigationRouteId.PROFILE, "Profile", "Your account identity and profile details.");
        put(map, NavigationRouteId.PREFERENCES, "Preferences", "Appearance, notifications, language, and shortcuts.");

        put(map, NavigationRouteId.UNKNOWN, "Unknown destination", "This navigation route is not recognized.");

        this.definitions = Map.copyOf(map);
    }

    private static void put(Map<NavigationRouteId, ViewDefinition> map, NavigationRouteId route, String title, String description) {
        map.put(route, new ViewDefinition(route, title, description));
    }

    @Override
    public ViewDefinition resolve(NavigationRouteId route) {
        if (route == null) {
            return definitions.get(NavigationRouteId.UNKNOWN);
        }
        return definitions.getOrDefault(route, definitions.get(NavigationRouteId.UNKNOWN));
    }
}
