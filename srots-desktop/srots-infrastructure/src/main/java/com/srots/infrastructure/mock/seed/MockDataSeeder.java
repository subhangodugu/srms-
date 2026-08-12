package com.srots.infrastructure.mock.seed;

import com.srots.domain.activity.ActivityEntry;
import com.srots.domain.customer.Customer;
import com.srots.domain.department.Department;
import com.srots.domain.employee.Employee;
import com.srots.domain.issue.Issue;
import com.srots.domain.model.Product;
import com.srots.domain.model.enums.*;
import com.srots.domain.notification.AppNotification;
import com.srots.domain.project.Project;
import com.srots.domain.release.Release;
import com.srots.domain.release.ReleasePipelineGate;
import com.srots.domain.sales.SalesDeal;
import com.srots.domain.sales.SalesLead;
import com.srots.domain.sales.SalesOpportunity;
import com.srots.domain.task.Task;
import com.srots.domain.team.Team;
import com.srots.domain.valueobject.ProductId;
import com.srots.domain.valueobject.VersionNumber;
import com.srots.domain.version.ProductVersion;
import com.srots.infrastructure.mock.configuration.MockConfiguration;
import com.srots.infrastructure.mock.configuration.MockScenarioType;
import com.srots.infrastructure.mock.state.MockStateStore;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Builds deterministic mock datasets and loads them into {@link MockStateStore}.
 */
public final class MockDataSeeder {

    public static final String PRODUCT_SROTS = "PROD-SROTS";
    public static final String PRODUCT_COMPTY = "PROD-COMPTY";

    private final MockConfiguration configuration;
    private final MockStateStore store;

    public MockDataSeeder(MockConfiguration configuration, MockStateStore store) {
        this.configuration = configuration;
        this.store = store;
    }

    public void seed() {
        seed(configuration.getScenario());
    }

    public void seed(MockScenarioType scenario) {
        store.clear();
        switch (scenario == null ? MockScenarioType.NORMAL : scenario) {
            case EMPTY -> { /* intentionally empty */ }
            case LARGE -> loadLarge();
            case ERROR, OFFLINE, LOADING, NORMAL -> loadNormal();
        }
    }

    public void reset() {
        seed(configuration.getScenario());
    }

    private void loadNormal() {
        LocalDate ref = configuration.getReferenceDate();
        List<Department> departments = seedDepartments();
        List<Team> teams = seedTeams();
        List<Employee> employees = seedEmployees(ref);
        List<Product> products = seedProducts(ref);
        List<ProductVersion> versions = seedVersions(ref);
        List<Project> projects = seedProjects(ref);
        List<Task> tasks = seedTasks(ref, projects, employees);
        List<Issue> issues = seedIssues(ref, projects, employees);
        List<Release> releases = seedReleases(ref);
        List<Customer> customers = seedCustomers();
        List<SalesLead> leads = seedLeads(ref);
        List<SalesOpportunity> opportunities = seedOpportunities(ref);
        List<SalesDeal> deals = seedDeals(ref);
        List<AppNotification> notifications = seedNotifications(ref);
        List<ActivityEntry> activities = seedActivities(ref);

        store.replaceAll(departments, teams, employees, products, versions, projects, tasks, issues,
                releases, customers, leads, opportunities, deals, notifications, activities);
    }

    private void loadLarge() {
        loadNormal();
        LocalDate ref = configuration.getReferenceDate();
        for (int i = 1; i <= 1000; i++) {
            String id = "EMP-L" + String.format("%04d", i);
            store.putEmployee(new Employee(
                    id,
                    "Load Tester " + i,
                    "load.tester" + i + "@srots.example",
                    i % 2 == 0 ? "Software Engineer" : "QA Engineer",
                    "DEPT-ENG",
                    "TEAM-BACKEND",
                    EmployeeStatus.ACTIVE,
                    ref.minusDays(i % 900),
                    "EMP-001",
                    "Remote",
                    "LT"));
        }
        for (int i = 1; i <= 5000; i++) {
            store.putTask(new Task(
                    "TSK-L" + String.format("%05d", i),
                    "Bulk task " + i,
                    "PRJ-001",
                    "EMP-00" + ((i % 9) + 1),
                    TaskStatus.TODO,
                    TaskPriority.MEDIUM,
                    ref.plusDays(i % 60),
                    i % 100,
                    ref.minusDays(i % 120)));
        }
        for (int i = 1; i <= 1000; i++) {
            store.putIssue(new Issue(
                    "ISS-L" + String.format("%04d", i),
                    "Bulk issue " + i,
                    "Deterministic large-dataset issue",
                    "PRJ-001",
                    PRODUCT_COMPTY,
                    "EMP-002",
                    "EMP-003",
                    IssuePriority.MEDIUM,
                    IssueSeverity.MINOR,
                    IssueStatus.OPEN,
                    ref.minusDays(i % 90),
                    ref.minusDays(i % 30)));
        }
    }

    private List<Department> seedDepartments() {
        return List.of(
                new Department("DEPT-ENG", "Engineering", "ENG", "EMP-001"),
                new Department("DEPT-PROD", "Product", "PROD", "EMP-010"),
                new Department("DEPT-QA", "Quality Assurance", "QA", "EMP-012"),
                new Department("DEPT-DEVOPS", "DevOps", "DOPS", "EMP-014"),
                new Department("DEPT-SALES", "Sales", "SALES", "EMP-016"),
                new Department("DEPT-MKT", "Marketing", "MKT", "EMP-018"),
                new Department("DEPT-CS", "Customer Support", "CS", "EMP-020"),
                new Department("DEPT-FIN", "Finance", "FIN", "EMP-022"),
                new Department("DEPT-HR", "Human Resources", "HR", "EMP-024"),
                new Department("DEPT-ADMIN", "Administration", "ADMIN", "EMP-025")
        );
    }

    private List<Team> seedTeams() {
        return List.of(
                new Team("TEAM-FRONTEND", "Frontend", "DEPT-ENG", "EMP-002"),
                new Team("TEAM-BACKEND", "Backend", "DEPT-ENG", "EMP-003"),
                new Team("TEAM-QA", "QA", "DEPT-QA", "EMP-012"),
                new Team("TEAM-DEVOPS", "DevOps", "DEPT-DEVOPS", "EMP-014"),
                new Team("TEAM-PLATFORM", "Platform", "DEPT-ENG", "EMP-004"),
                new Team("TEAM-AIML", "AI/ML", "DEPT-ENG", "EMP-005"),
                new Team("TEAM-PRODUCT", "Product", "DEPT-PROD", "EMP-010"),
                new Team("TEAM-SALES", "Sales", "DEPT-SALES", "EMP-016"),
                new Team("TEAM-CS", "Customer Success", "DEPT-CS", "EMP-020"),
                new Team("TEAM-SUPPORT", "Support", "DEPT-CS", "EMP-021")
        );
    }

    private List<Employee> seedEmployees(LocalDate ref) {
        record Emp(String id, String name, String email, String title, String dept, String team, String manager, String loc) {}
        Emp[] defs = new Emp[] {
                new Emp("EMP-001", "Aisha Rahman", "aisha.rahman@srots.example", "Engineering Manager", "DEPT-ENG", "TEAM-PLATFORM", null, "Bengaluru"),
                new Emp("EMP-002", "Noah Patel", "noah.patel@srots.example", "Technical Lead", "DEPT-ENG", "TEAM-FRONTEND", "EMP-001", "Bengaluru"),
                new Emp("EMP-003", "Maya Chen", "maya.chen@srots.example", "Senior Software Engineer", "DEPT-ENG", "TEAM-BACKEND", "EMP-001", "Hyderabad"),
                new Emp("EMP-004", "Liam Okonkwo", "liam.okonkwo@srots.example", "Software Engineer", "DEPT-ENG", "TEAM-PLATFORM", "EMP-001", "Pune"),
                new Emp("EMP-005", "Sofia Alvarez", "sofia.alvarez@srots.example", "Software Engineer", "DEPT-ENG", "TEAM-AIML", "EMP-001", "Remote"),
                new Emp("EMP-006", "Ethan Brooks", "ethan.brooks@srots.example", "Senior Software Engineer", "DEPT-ENG", "TEAM-BACKEND", "EMP-003", "Chennai"),
                new Emp("EMP-007", "Priya Nair", "priya.nair@srots.example", "UI/UX Designer", "DEPT-PROD", "TEAM-PRODUCT", "EMP-010", "Bengaluru"),
                new Emp("EMP-008", "Jonas Meyer", "jonas.meyer@srots.example", "Software Engineer", "DEPT-ENG", "TEAM-FRONTEND", "EMP-002", "Berlin"),
                new Emp("EMP-009", "Hana Suzuki", "hana.suzuki@srots.example", "Software Engineer", "DEPT-ENG", "TEAM-BACKEND", "EMP-003", "Tokyo"),
                new Emp("EMP-010", "Elena Petrova", "elena.petrova@srots.example", "Product Manager", "DEPT-PROD", "TEAM-PRODUCT", null, "London"),
                new Emp("EMP-011", "Omar Haddad", "omar.haddad@srots.example", "Product Manager", "DEPT-PROD", "TEAM-PRODUCT", "EMP-010", "Dubai"),
                new Emp("EMP-012", "Grace Kim", "grace.kim@srots.example", "QA Engineer", "DEPT-QA", "TEAM-QA", "EMP-001", "Seoul"),
                new Emp("EMP-013", "Lucas Ferreira", "lucas.ferreira@srots.example", "QA Engineer", "DEPT-QA", "TEAM-QA", "EMP-012", "Sao Paulo"),
                new Emp("EMP-014", "Amelia Wright", "amelia.wright@srots.example", "DevOps Engineer", "DEPT-DEVOPS", "TEAM-DEVOPS", "EMP-001", "Austin"),
                new Emp("EMP-015", "Kenji Watanabe", "kenji.watanabe@srots.example", "DevOps Engineer", "DEPT-DEVOPS", "TEAM-DEVOPS", "EMP-014", "Osaka"),
                new Emp("EMP-016", "Isabella Rossi", "isabella.rossi@srots.example", "Sales Executive", "DEPT-SALES", "TEAM-SALES", null, "Milan"),
                new Emp("EMP-017", "Daniel Costa", "daniel.costa@srots.example", "Sales Executive", "DEPT-SALES", "TEAM-SALES", "EMP-016", "Lisbon"),
                new Emp("EMP-018", "Chloe Martin", "chloe.martin@srots.example", "Marketing Specialist", "DEPT-MKT", "TEAM-PRODUCT", null, "Paris"),
                new Emp("EMP-019", "Arjun Mehta", "arjun.mehta@srots.example", "Support Engineer", "DEPT-CS", "TEAM-SUPPORT", "EMP-020", "Mumbai"),
                new Emp("EMP-020", "Fatima Zahra", "fatima.zahra@srots.example", "Support Engineer", "DEPT-CS", "TEAM-CS", null, "Casablanca"),
                new Emp("EMP-021", "Ryan Thompson", "ryan.thompson@srots.example", "Support Engineer", "DEPT-CS", "TEAM-SUPPORT", "EMP-020", "Toronto"),
                new Emp("EMP-022", "Nina Johansson", "nina.johansson@srots.example", "Finance Analyst", "DEPT-FIN", "TEAM-PRODUCT", null, "Stockholm"),
                new Emp("EMP-023", "Carlos Mendoza", "carlos.mendoza@srots.example", "Software Engineer", "DEPT-ENG", "TEAM-FRONTEND", "EMP-002", "Mexico City"),
                new Emp("EMP-024", "Hannah Levine", "hannah.levine@srots.example", "HR Business Partner", "DEPT-HR", "TEAM-PRODUCT", null, "New York"),
                new Emp("EMP-025", "Victor Hugo", "victor.hugo@srots.example", "Office Administrator", "DEPT-ADMIN", "TEAM-PRODUCT", null, "Bengaluru"),
                new Emp("EMP-026", "Mei Lin", "mei.lin@srots.example", "Senior Software Engineer", "DEPT-ENG", "TEAM-AIML", "EMP-005", "Singapore"),
                new Emp("EMP-027", "Samuel Adeyemi", "samuel.adeyemi@srots.example", "QA Engineer", "DEPT-QA", "TEAM-QA", "EMP-012", "Lagos"),
                new Emp("EMP-028", "Olivia Brown", "olivia.brown@srots.example", "DevOps Engineer", "DEPT-DEVOPS", "TEAM-DEVOPS", "EMP-014", "Remote"),
                new Emp("EMP-029", "Ibrahim Khan", "ibrahim.khan@srots.example", "Software Engineer", "DEPT-ENG", "TEAM-BACKEND", "EMP-003", "Karachi"),
                new Emp("EMP-030", "Emma Wilson", "emma.wilson@srots.example", "UI/UX Designer", "DEPT-PROD", "TEAM-PRODUCT", "EMP-010", "Sydney")
        };
        List<Employee> list = new ArrayList<>();
        for (int i = 0; i < defs.length; i++) {
            Emp e = defs[i];
            EmployeeStatus status = i == 24 ? EmployeeStatus.ONBOARDING : (i == 21 ? EmployeeStatus.ON_LEAVE : EmployeeStatus.ACTIVE);
            list.add(new Employee(e.id, e.name, e.email, e.title, e.dept, e.team, status,
                    ref.minusDays(30L + i * 17L), e.manager, e.loc, initials(e.name)));
        }
        return list;
    }

    private static String initials(String name) {
        String[] parts = name.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (!p.isBlank()) sb.append(Character.toUpperCase(p.charAt(0)));
        }
        return sb.length() >= 2 ? sb.substring(0, 2) : sb.toString();
    }

    private List<Product> seedProducts(LocalDate ref) {
        return List.of(
                new Product(new ProductId(PRODUCT_SROTS), "SROTS", "SROTS",
                        "Enterprise control plane for company operations and product lifecycle.",
                        new VersionNumber("0.2.0"), new VersionNumber("0.3.0"),
                        "Aisha Rahman", "Enterprise Platform", ProductStatus.ACTIVE,
                        "TEAM-PLATFORM", "https://git.srots.example/srots", ref.minusYears(1)),
                new Product(new ProductId(PRODUCT_COMPTY), "COMPTY", "COMPTY",
                        "ATE Intelligence Platform managed by SROTS.",
                        new VersionNumber("1.9.0"), new VersionNumber("2.0.0"),
                        "Elena Petrova", "ATE Intelligence Platform", ProductStatus.ACTIVE,
                        "TEAM-BACKEND", "https://git.srots.example/compty", ref.minusYears(3))
        );
    }

    private List<ProductVersion> seedVersions(LocalDate ref) {
        return List.of(
                new ProductVersion("VER-S-010", PRODUCT_SROTS, "0.1.0", VersionStatus.RELEASED, ref.minusMonths(6), ref.minusMonths(6), 24, 8, 100),
                new ProductVersion("VER-S-020", PRODUCT_SROTS, "0.2.0", VersionStatus.RELEASED, ref.minusMonths(2), ref.minusMonths(2), 31, 5, 100),
                new ProductVersion("VER-S-030", PRODUCT_SROTS, "0.3.0", VersionStatus.IN_DEVELOPMENT, null, ref.plusMonths(1), 28, 12, 62),
                new ProductVersion("VER-C-170", PRODUCT_COMPTY, "1.7.0", VersionStatus.RELEASED, ref.minusMonths(9), ref.minusMonths(9), 40, 11, 100),
                new ProductVersion("VER-C-180", PRODUCT_COMPTY, "1.8.0", VersionStatus.RELEASED, ref.minusMonths(5), ref.minusMonths(5), 37, 9, 100),
                new ProductVersion("VER-C-190", PRODUCT_COMPTY, "1.9.0", VersionStatus.RELEASE_CANDIDATE, null, ref.plusDays(10), 42, 7, 91),
                new ProductVersion("VER-C-200", PRODUCT_COMPTY, "2.0.0", VersionStatus.PLANNED, null, ref.plusMonths(4), 55, 0, 18)
        );
    }

    private List<Project> seedProjects(LocalDate ref) {
        String[][] defs = {
                {"PRJ-001", "COMPTY Release 1.9 Hardening", "Stabilize COMPTY 1.9 for customer rollout", PRODUCT_COMPTY, "EMP-010", "TEAM-BACKEND", "ACTIVE", "HIGH", "72"},
                {"PRJ-002", "SROTS Desktop Shell", "JavaFX shell, navigation, and design system", PRODUCT_SROTS, "EMP-002", "TEAM-FRONTEND", "ACTIVE", "CRITICAL", "68"},
                {"PRJ-003", "COMPTY Analytics Pipeline", "Operational analytics for COMPTY customers", PRODUCT_COMPTY, "EMP-005", "TEAM-AIML", "ACTIVE", "HIGH", "45"},
                {"PRJ-004", "Identity and Access Foundation", "Session and permission architecture prep", PRODUCT_SROTS, "EMP-003", "TEAM-PLATFORM", "PLANNED", "HIGH", "12"},
                {"PRJ-005", "Release Gate Automation", "Automate QA and security release gates", PRODUCT_COMPTY, "EMP-014", "TEAM-DEVOPS", "AT_RISK", "CRITICAL", "55"},
                {"PRJ-006", "Customer Success Playbooks", "Support playbooks for COMPTY onboarding", PRODUCT_COMPTY, "EMP-020", "TEAM-CS", "ACTIVE", "MEDIUM", "40"},
                {"PRJ-007", "Sales Opportunity Workspace", "Desktop sales pipeline screens", PRODUCT_SROTS, "EMP-016", "TEAM-SALES", "PLANNED", "MEDIUM", "8"},
                {"PRJ-008", "Knowledge Base Refresh", "Rewrite SOPs for support and engineering", PRODUCT_SROTS, "EMP-019", "TEAM-SUPPORT", "ON_HOLD", "LOW", "22"},
                {"PRJ-009", "COMPTY Requirements Sync", "Requirements traceability for 2.0", PRODUCT_COMPTY, "EMP-011", "TEAM-PRODUCT", "ACTIVE", "HIGH", "33"},
                {"PRJ-010", "Platform Observability", "Metrics and audit readiness for SROTS", PRODUCT_SROTS, "EMP-004", "TEAM-PLATFORM", "ACTIVE", "MEDIUM", "50"},
                {"PRJ-011", "QA Regression Suite 1.9", "Expanded automated regression for COMPTY", PRODUCT_COMPTY, "EMP-012", "TEAM-QA", "ACTIVE", "HIGH", "70"},
                {"PRJ-012", "Deployment Runbook 2.0", "Blue/green deployment docs and tooling", PRODUCT_COMPTY, "EMP-015", "TEAM-DEVOPS", "PLANNED", "MEDIUM", "15"},
                {"PRJ-013", "Employee Directory UX", "Company employees module UI", PRODUCT_SROTS, "EMP-007", "TEAM-FRONTEND", "ACTIVE", "MEDIUM", "38"},
                {"PRJ-014", "Contract Renewal Tracker", "Customer contract status dashboards", PRODUCT_SROTS, "EMP-017", "TEAM-SALES", "BLOCKED", "HIGH", "20"},
                {"PRJ-015", "COMPTY Mobile Companion Spike", "Feasibility for field technicians", PRODUCT_COMPTY, "EMP-010", "TEAM-PRODUCT", "COMPLETED", "LOW", "100"}
        };
        List<Project> list = new ArrayList<>();
        for (int i = 0; i < defs.length; i++) {
            String[] d = defs[i];
            list.add(new Project(d[0], d[1], d[2], d[3], d[4], d[5],
                    ProjectStatus.valueOf(d[6]), ProjectPriority.valueOf(d[7]),
                    ref.minusDays(90 - i * 4L), ref.plusDays(20 + i * 3L), Integer.parseInt(d[8])));
        }
        return list;
    }

    private List<Task> seedTasks(LocalDate ref, List<Project> projects, List<Employee> employees) {
        List<Task> tasks = new ArrayList<>();
        String[] titles = {
                "Finalize navigation breadcrumbs", "Wire employee table pagination", "Add release gate badges",
                "Implement mock latency toggle", "Write QA checklist for 1.9", "Document deployment rollback",
                "Polish overview KPI cards", "Fix offline banner copy", "Add customer search filters",
                "Prepare COMPTY analytics sample", "Review security findings", "Sync requirements backlog",
                "Update design system tokens", "Create sales stage chips", "Harden CI pipeline"
        };
        TaskStatus[] statuses = TaskStatus.values();
        TaskPriority[] priorities = TaskPriority.values();
        for (int i = 1; i <= 75; i++) {
            Project project = projects.get((i - 1) % projects.size());
            Employee assignee = employees.get((i + 2) % employees.size());
            tasks.add(new Task(
                    "TSK-" + String.format("%03d", i),
                    titles[(i - 1) % titles.length] + " (" + project.getId() + ")",
                    project.getId(),
                    assignee.getId(),
                    statuses[i % statuses.length],
                    priorities[i % priorities.length],
                    ref.plusDays((i % 40) - 10),
                    (i * 7) % 100,
                    ref.minusDays(i % 45)));
        }
        return tasks;
    }

    private List<Issue> seedIssues(LocalDate ref, List<Project> projects, List<Employee> employees) {
        List<Issue> issues = new ArrayList<>();
        String[] titles = {
                "Sidebar selection desync on deep route", "Release approval warning not dismissible",
                "COMPTY analytics empty chart on cold start", "Task due date sorting inverted",
                "Notification badge count stale", "Search ignores hyphenated names",
                "QA gate shows pending after pass", "Customer filter resets on refresh",
                "Offline banner overlaps topbar", "Large table scroll hitch",
                "Version progress exceeds 100", "Deal stage chip color mismatch"
        };
        for (int i = 1; i <= 45; i++) {
            Project project = projects.get((i - 1) % projects.size());
            issues.add(new Issue(
                    "ISS-" + String.format("%03d", i),
                    titles[(i - 1) % titles.length],
                    "Synthetic issue for frontend development and QA.",
                    project.getId(),
                    project.getProductId(),
                    employees.get(i % employees.size()).getId(),
                    employees.get((i + 5) % employees.size()).getId(),
                    IssuePriority.values()[i % IssuePriority.values().length],
                    IssueSeverity.values()[i % IssueSeverity.values().length],
                    IssueStatus.values()[i % IssueStatus.values().length],
                    ref.minusDays(i % 60),
                    ref.minusDays(i % 20)));
        }
        return issues;
    }

    private List<Release> seedReleases(LocalDate ref) {
        List<ReleasePipelineGate> comptyGates = List.of(
                new ReleasePipelineGate("Development", GatePhaseStatus.PASSED, 1),
                new ReleasePipelineGate("Unit Tests", GatePhaseStatus.PASSED, 2),
                new ReleasePipelineGate("Integration", GatePhaseStatus.PASSED, 3),
                new ReleasePipelineGate("QA", GatePhaseStatus.PASSED, 4),
                new ReleasePipelineGate("Security", GatePhaseStatus.PASSED, 5),
                new ReleasePipelineGate("Approval", GatePhaseStatus.WARNING, 6),
                new ReleasePipelineGate("Deployment", GatePhaseStatus.PENDING, 7),
                new ReleasePipelineGate("Production", GatePhaseStatus.PENDING, 8)
        );
        return List.of(
                new Release("REL-001", PRODUCT_COMPTY, "VER-C-190", "COMPTY v1.9.0",
                        ReleaseStatus.READY_FOR_APPROVAL, "EMP-010", ref.plusDays(10),
                        GatePhaseStatus.PASSED, GatePhaseStatus.PASSED, GatePhaseStatus.WARNING,
                        GatePhaseStatus.PENDING, 87, comptyGates),
                new Release("REL-002", PRODUCT_COMPTY, "VER-C-180", "COMPTY v1.8.0",
                        ReleaseStatus.RELEASED, "EMP-010", ref.minusMonths(5),
                        GatePhaseStatus.PASSED, GatePhaseStatus.PASSED, GatePhaseStatus.PASSED,
                        GatePhaseStatus.PASSED, 100, List.of()),
                new Release("REL-003", PRODUCT_SROTS, "VER-S-030", "SROTS v0.3.0",
                        ReleaseStatus.DEVELOPMENT, "EMP-002", ref.plusMonths(1),
                        GatePhaseStatus.PENDING, GatePhaseStatus.PENDING, GatePhaseStatus.PENDING,
                        GatePhaseStatus.PENDING, 42, List.of(
                        new ReleasePipelineGate("Development", GatePhaseStatus.PASSED, 1),
                        new ReleasePipelineGate("Unit Tests", GatePhaseStatus.WARNING, 2),
                        new ReleasePipelineGate("Integration", GatePhaseStatus.PENDING, 3),
                        new ReleasePipelineGate("QA", GatePhaseStatus.PENDING, 4),
                        new ReleasePipelineGate("Security", GatePhaseStatus.PENDING, 5),
                        new ReleasePipelineGate("Approval", GatePhaseStatus.PENDING, 6),
                        new ReleasePipelineGate("Deployment", GatePhaseStatus.PENDING, 7),
                        new ReleasePipelineGate("Production", GatePhaseStatus.PENDING, 8)
                )),
                new Release("REL-004", PRODUCT_SROTS, "VER-S-020", "SROTS v0.2.0",
                        ReleaseStatus.RELEASED, "EMP-001", ref.minusMonths(2),
                        GatePhaseStatus.PASSED, GatePhaseStatus.PASSED, GatePhaseStatus.PASSED,
                        GatePhaseStatus.PASSED, 100, List.of()),
                new Release("REL-005", PRODUCT_COMPTY, "VER-C-200", "COMPTY v2.0.0",
                        ReleaseStatus.PLANNED, "EMP-011", ref.plusMonths(4),
                        GatePhaseStatus.PENDING, GatePhaseStatus.PENDING, GatePhaseStatus.PENDING,
                        GatePhaseStatus.PENDING, 10, List.of())
        );
    }

    private List<Customer> seedCustomers() {
        String[][] defs = {
                {"CUS-001", "Northwind Manufacturing", "Manufacturing", "Helen Carter", "helen.carter@northwind.example", "ACTIVE", PRODUCT_COMPTY, "EMP-016", "ACTIVE"},
                {"CUS-002", "Atlas Logistics", "Logistics", "Ben Ortiz", "ben.ortiz@atlas.example", "ACTIVE", PRODUCT_COMPTY, "EMP-017", "ACTIVE"},
                {"CUS-003", "Helios Energy", "Energy", "Sara Quinn", "sara.quinn@helios.example", "PROSPECT", PRODUCT_COMPTY, "EMP-016", "DRAFT"},
                {"CUS-004", "Blue Harbor Hospitals", "Healthcare", "Dr. Patel", "admin@blueharbor.example", "ACTIVE", PRODUCT_COMPTY, "EMP-017", "RENEWAL"},
                {"CUS-005", "Summit Municipal IT", "Public Sector", "Chris Lang", "clang@summit.gov.example", "CHURN_RISK", PRODUCT_COMPTY, "EMP-016", "ACTIVE"},
                {"CUS-006", "Orbit Aerospace", "Aerospace", "Nina Park", "nina.park@orbit.example", "ACTIVE", PRODUCT_COMPTY, "EMP-017", "ACTIVE"},
                {"CUS-007", "Cedar Retail Group", "Retail", "Tom Reed", "tom.reed@cedar.example", "INACTIVE", PRODUCT_SROTS, "EMP-016", "EXPIRED"},
                {"CUS-008", "Vertex Semiconductors", "Electronics", "Aya Mori", "aya.mori@vertex.example", "ACTIVE", PRODUCT_COMPTY, "EMP-017", "ACTIVE"},
                {"CUS-009", "Prairie AgriTech", "Agriculture", "Owen Blake", "owen.blake@prairie.example", "PROSPECT", PRODUCT_COMPTY, "EMP-016", "DRAFT"},
                {"CUS-010", "Lumen Transit", "Transportation", "Rita Gomez", "rita.gomez@lumen.example", "ACTIVE", PRODUCT_COMPTY, "EMP-017", "ACTIVE"},
                {"CUS-011", "Nova University Labs", "Education", "Prof. Ng", "lab@nova.edu.example", "ACTIVE", PRODUCT_SROTS, "EMP-016", "ACTIVE"},
                {"CUS-012", "Cascade Utilities", "Utilities", "Mark Ellis", "mark.ellis@cascade.example", "ACTIVE", PRODUCT_COMPTY, "EMP-017", "ACTIVE"},
                {"CUS-013", "Silverline Banking", "Finance", "Zoe Hart", "zoe.hart@silverline.example", "PROSPECT", PRODUCT_SROTS, "EMP-016", "DRAFT"},
                {"CUS-014", "Harbor Defense Systems", "Defense", "Lt. Ames", "ames@harbordef.example", "ACTIVE", PRODUCT_COMPTY, "EMP-017", "ACTIVE"},
                {"CUS-015", "Greenfield Foods", "Food and Beverage", "Paula Ng", "paula.ng@greenfield.example", "ACTIVE", PRODUCT_COMPTY, "EMP-016", "ACTIVE"}
        };
        List<Customer> list = new ArrayList<>();
        for (String[] d : defs) {
            list.add(new Customer(d[0], d[1], d[2], d[3], d[4],
                    CustomerStatus.valueOf(d[5]), d[6], d[7], ContractStatus.valueOf(d[8])));
        }
        return list;
    }

    private List<SalesLead> seedLeads(LocalDate ref) {
        return List.of(
                new SalesLead("LED-001", "Polaris Robotics", "Jordan Lee", "j.lee@polaris.example", SalesStage.NEW, PRODUCT_COMPTY, "EMP-016", ref.minusDays(3)),
                new SalesLead("LED-002", "Quanta Labs", "Mia Torres", "mia@quanta.example", SalesStage.QUALIFIED, PRODUCT_COMPTY, "EMP-017", ref.minusDays(12)),
                new SalesLead("LED-003", "Ironclad Mining", "Seth Cole", "seth@ironclad.example", SalesStage.PROPOSAL, PRODUCT_COMPTY, "EMP-016", ref.minusDays(20)),
                new SalesLead("LED-004", "Brightline Media", "Ana Ruiz", "ana@brightline.example", SalesStage.NEW, PRODUCT_SROTS, "EMP-017", ref.minusDays(5)),
                new SalesLead("LED-005", "Alpine MedTech", "Chris Young", "chris@alpine.example", SalesStage.NEGOTIATION, PRODUCT_COMPTY, "EMP-016", ref.minusDays(28)),
                new SalesLead("LED-006", "Coastal Ports Authority", "Dana Fox", "dana@cpa.example", SalesStage.QUALIFIED, PRODUCT_COMPTY, "EMP-017", ref.minusDays(9)),
                new SalesLead("LED-007", "Nimbus Cloud Co", "Lee Park", "lee@nimbus.example", SalesStage.LOST, PRODUCT_SROTS, "EMP-016", ref.minusDays(40)),
                new SalesLead("LED-008", "Falcon Safety", "Rita Shaw", "rita@falcon.example", SalesStage.NEW, PRODUCT_COMPTY, "EMP-017", ref.minusDays(2))
        );
    }

    private List<SalesOpportunity> seedOpportunities(LocalDate ref) {
        return List.of(
                new SalesOpportunity("OPP-001", "Northwind COMPTY Expansion", "CUS-001", PRODUCT_COMPTY, SalesStage.NEGOTIATION, new BigDecimal("185000"), "EMP-016", ref.plusDays(25)),
                new SalesOpportunity("OPP-002", "Atlas Site Licenses", "CUS-002", PRODUCT_COMPTY, SalesStage.PROPOSAL, new BigDecimal("92000"), "EMP-017", ref.plusDays(40)),
                new SalesOpportunity("OPP-003", "Helios Pilot", "CUS-003", PRODUCT_COMPTY, SalesStage.QUALIFIED, new BigDecimal("45000"), "EMP-016", ref.plusDays(55)),
                new SalesOpportunity("OPP-004", "Blue Harbor Multi-site", "CUS-004", PRODUCT_COMPTY, SalesStage.NEGOTIATION, new BigDecimal("210000"), "EMP-017", ref.plusDays(18)),
                new SalesOpportunity("OPP-005", "Nova Labs SROTS Seat Pack", "CUS-011", PRODUCT_SROTS, SalesStage.PROPOSAL, new BigDecimal("38000"), "EMP-016", ref.plusDays(33)),
                new SalesOpportunity("OPP-006", "Vertex 2.0 Early Access", "CUS-008", PRODUCT_COMPTY, SalesStage.NEW, new BigDecimal("150000"), "EMP-017", ref.plusDays(70))
        );
    }

    private List<SalesDeal> seedDeals(LocalDate ref) {
        return List.of(
                new SalesDeal("DEA-001", "Orbit Aerospace Annual", "OPP-001", "CUS-006", PRODUCT_COMPTY, SalesStage.WON, new BigDecimal("240000"), "EMP-016", ref.minusDays(40)),
                new SalesDeal("DEA-002", "Cascade Utilities Renewal", "OPP-002", "CUS-012", PRODUCT_COMPTY, SalesStage.WON, new BigDecimal("110000"), "EMP-017", ref.minusDays(15)),
                new SalesDeal("DEA-003", "Cedar Retail Exit", "OPP-003", "CUS-007", PRODUCT_SROTS, SalesStage.LOST, new BigDecimal("27000"), "EMP-016", ref.minusDays(55)),
                new SalesDeal("DEA-004", "Lumen Transit Expansion", "OPP-004", "CUS-010", PRODUCT_COMPTY, SalesStage.WON, new BigDecimal("98000"), "EMP-017", ref.minusDays(8))
        );
    }

    private List<AppNotification> seedNotifications(LocalDate ref) {
        LocalDateTime base = ref.atTime(9, 0);
        return List.of(
                new AppNotification("NTF-001", "Release approved", "COMPTY v1.8.0 received final approval.", NotificationType.RELEASE, true, base.minusHours(30), "RELEASE", "REL-002"),
                new AppNotification("NTF-002", "New task assigned", "You were assigned TSK-014.", NotificationType.ASSIGNMENT, false, base.minusHours(5), "TASK", "TSK-014"),
                new AppNotification("NTF-003", "Build failed", "Platform CI failed on main.", NotificationType.BUILD, false, base.minusHours(2), "PROJECT", "PRJ-010"),
                new AppNotification("NTF-004", "QA gate completed", "COMPTY v1.9.0 QA gate passed.", NotificationType.SUCCESS, false, base.minusHours(8), "RELEASE", "REL-001"),
                new AppNotification("NTF-005", "New support ticket", "Atlas Logistics opened ticket SUP-441.", NotificationType.SUPPORT, false, base.minusHours(1), "CUSTOMER", "CUS-002"),
                new AppNotification("NTF-006", "Project deadline approaching", "Release Gate Automation is due soon.", NotificationType.WARNING, false, base.minusHours(12), "PROJECT", "PRJ-005"),
                new AppNotification("NTF-007", "Security scan clean", "No critical findings for SROTS 0.2.0.", NotificationType.SUCCESS, true, base.minusDays(3), "RELEASE", "REL-004"),
                new AppNotification("NTF-008", "Deal won", "Orbit Aerospace annual deal closed.", NotificationType.SUCCESS, true, base.minusDays(2), "DEAL", "DEA-001"),
                new AppNotification("NTF-009", "Issue reopened", "ISS-003 was reopened by QA.", NotificationType.ERROR, false, base.minusHours(16), "ISSUE", "ISS-003"),
                new AppNotification("NTF-010", "Deployment scheduled", "COMPTY v1.9.0 pending approval.", NotificationType.INFO, false, base.minusMinutes(40), "RELEASE", "REL-001"),
                new AppNotification("NTF-011", "Employee onboarded", "Victor Hugo joined Administration.", NotificationType.INFO, true, base.minusDays(1), "EMPLOYEE", "EMP-025"),
                new AppNotification("NTF-012", "Offline sync pending", "3 local changes awaiting sync.", NotificationType.WARNING, false, base.minusMinutes(15), "SYSTEM", "SYNC"),
                new AppNotification("NTF-013", "Customer churn risk", "Summit Municipal IT flagged.", NotificationType.WARNING, false, base.minusHours(20), "CUSTOMER", "CUS-005"),
                new AppNotification("NTF-014", "Version created", "SROTS 0.3.0 version record created.", NotificationType.PRODUCT, true, base.minusDays(4), "VERSION", "VER-S-030"),
                new AppNotification("NTF-015", "Task completed", "TSK-002 marked done.", NotificationType.SUCCESS, true, base.minusHours(26), "TASK", "TSK-002")
        );
    }

    private List<ActivityEntry> seedActivities(LocalDate ref) {
        LocalDateTime base = ref.atTime(10, 0);
        return List.of(
                new ActivityEntry("ACT-001", ActivityType.PROJECT, "Elena Petrova updated COMPTY Release 1.9 Hardening", "EMP-010", "PROJECT", "PRJ-001", base.minusHours(3)),
                new ActivityEntry("ACT-002", ActivityType.RELEASE, "Release status changed to READY_FOR_APPROVAL", "EMP-012", "RELEASE", "REL-001", base.minusHours(6)),
                new ActivityEntry("ACT-003", ActivityType.TASK, "Noah Patel assigned TSK-014", "EMP-002", "TASK", "TSK-014", base.minusHours(5)),
                new ActivityEntry("ACT-004", ActivityType.ISSUE, "Grace Kim resolved ISS-011", "EMP-012", "ISSUE", "ISS-011", base.minusHours(9)),
                new ActivityEntry("ACT-005", ActivityType.QA, "QA approved COMPTY 1.9 build", "EMP-013", "RELEASE", "REL-001", base.minusHours(8)),
                new ActivityEntry("ACT-006", ActivityType.PRODUCT, "Product version SROTS 0.3.0 created", "EMP-001", "VERSION", "VER-S-030", base.minusDays(4)),
                new ActivityEntry("ACT-007", ActivityType.EMPLOYEE, "Hannah Levine onboarded Victor Hugo", "EMP-024", "EMPLOYEE", "EMP-025", base.minusDays(1)),
                new ActivityEntry("ACT-008", ActivityType.RELEASE, "Amelia Wright updated deployment gate", "EMP-014", "RELEASE", "REL-001", base.minusHours(2)),
                new ActivityEntry("ACT-009", ActivityType.PROJECT, "Maya Chen marked Platform Observability active", "EMP-003", "PROJECT", "PRJ-010", base.minusDays(2)),
                new ActivityEntry("ACT-010", ActivityType.SYSTEM, "Mock dataset seeded (version 1.0)", null, "SYSTEM", "MOCK", base.minusMinutes(1)),
                new ActivityEntry("ACT-011", ActivityType.TASK, "Priya Nair completed design review task", "EMP-007", "TASK", "TSK-002", base.minusHours(26)),
                new ActivityEntry("ACT-012", ActivityType.ISSUE, "Issue ISS-003 reopened after regression", "EMP-027", "ISSUE", "ISS-003", base.minusHours(16)),
                new ActivityEntry("ACT-013", ActivityType.PRODUCT, "COMPTY next version set to 2.0.0", "EMP-011", "PRODUCT", PRODUCT_COMPTY, base.minusDays(5)),
                new ActivityEntry("ACT-014", ActivityType.PROJECT, "Sales Opportunity Workspace planned", "EMP-016", "PROJECT", "PRJ-007", base.minusDays(7)),
                new ActivityEntry("ACT-015", ActivityType.QA, "Regression suite coverage updated", "EMP-012", "PROJECT", "PRJ-011", base.minusHours(14)),
                new ActivityEntry("ACT-016", ActivityType.RELEASE, "SROTS v0.2.0 marked RELEASED", "EMP-001", "RELEASE", "REL-004", base.minusMonths(2).withHour(11)),
                new ActivityEntry("ACT-017", ActivityType.EMPLOYEE, "Aisha Rahman updated engineering roster", "EMP-001", "EMPLOYEE", "EMP-029", base.minusDays(3)),
                new ActivityEntry("ACT-018", ActivityType.SYSTEM, "Offline banner shown for pending sync", null, "SYSTEM", "SYNC", base.minusMinutes(15)),
                new ActivityEntry("ACT-019", ActivityType.TASK, "Kenji Watanabe documented rollback steps", "EMP-015", "TASK", "TSK-006", base.minusDays(1).withHour(15)),
                new ActivityEntry("ACT-020", ActivityType.PROJECT, "Contract Renewal Tracker blocked on data feed", "EMP-017", "PROJECT", "PRJ-014", base.minusHours(22))
        );
    }
}
