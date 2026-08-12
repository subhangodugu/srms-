package com.srots.infrastructure.mock.state;

import com.srots.domain.activity.ActivityEntry;
import com.srots.domain.customer.Customer;
import com.srots.domain.department.Department;
import com.srots.domain.employee.Employee;
import com.srots.domain.issue.Issue;
import com.srots.domain.model.Product;
import com.srots.domain.notification.AppNotification;
import com.srots.domain.project.Project;
import com.srots.domain.release.Release;
import com.srots.domain.sales.SalesDeal;
import com.srots.domain.sales.SalesLead;
import com.srots.domain.sales.SalesOpportunity;
import com.srots.domain.task.Task;
import com.srots.domain.team.Team;
import com.srots.domain.version.ProductVersion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

/**
 * Mutable in-memory dataset for development mock repositories.
 */
public final class MockStateStore {

    private final Map<String, Department> departments = new LinkedHashMap<>();
    private final Map<String, Team> teams = new LinkedHashMap<>();
    private final Map<String, Employee> employees = new LinkedHashMap<>();
    private final Map<String, Product> products = new LinkedHashMap<>();
    private final Map<String, ProductVersion> versions = new LinkedHashMap<>();
    private final Map<String, Project> projects = new LinkedHashMap<>();
    private final Map<String, Task> tasks = new LinkedHashMap<>();
    private final Map<String, Issue> issues = new LinkedHashMap<>();
    private final Map<String, Release> releases = new LinkedHashMap<>();
    private final Map<String, Customer> customers = new LinkedHashMap<>();
    private final Map<String, SalesLead> leads = new LinkedHashMap<>();
    private final Map<String, SalesOpportunity> opportunities = new LinkedHashMap<>();
    private final Map<String, SalesDeal> deals = new LinkedHashMap<>();
    private final Map<String, AppNotification> notifications = new LinkedHashMap<>();
    private final Map<String, ActivityEntry> activities = new LinkedHashMap<>();
    private final List<Runnable> changeListeners = new CopyOnWriteArrayList<>();

    public synchronized void clear() {
        departments.clear();
        teams.clear();
        employees.clear();
        products.clear();
        versions.clear();
        projects.clear();
        tasks.clear();
        issues.clear();
        releases.clear();
        customers.clear();
        leads.clear();
        opportunities.clear();
        deals.clear();
        notifications.clear();
        activities.clear();
        notifyChanged();
    }

    public synchronized void replaceAll(
            List<Department> departments,
            List<Team> teams,
            List<Employee> employees,
            List<Product> products,
            List<ProductVersion> versions,
            List<Project> projects,
            List<Task> tasks,
            List<Issue> issues,
            List<Release> releases,
            List<Customer> customers,
            List<SalesLead> leads,
            List<SalesOpportunity> opportunities,
            List<SalesDeal> deals,
            List<AppNotification> notifications,
            List<ActivityEntry> activities) {
        clear();
        putAll(this.departments, departments, Department::getId);
        putAll(this.teams, teams, Team::getId);
        putAll(this.employees, employees, Employee::getId);
        putAll(this.products, products, p -> p.getId().getValue());
        putAll(this.versions, versions, ProductVersion::getId);
        putAll(this.projects, projects, Project::getId);
        putAll(this.tasks, tasks, Task::getId);
        putAll(this.issues, issues, Issue::getId);
        putAll(this.releases, releases, Release::getId);
        putAll(this.customers, customers, Customer::getId);
        putAll(this.leads, leads, SalesLead::getId);
        putAll(this.opportunities, opportunities, SalesOpportunity::getId);
        putAll(this.deals, deals, SalesDeal::getId);
        putAll(this.notifications, notifications, AppNotification::getId);
        putAll(this.activities, activities, ActivityEntry::getId);
        notifyChanged();
    }

    private static <T> void putAll(Map<String, T> target, List<T> source, Function<T, String> idFn) {
        if (source == null) {
            return;
        }
        for (T item : source) {
            if (item != null) {
                target.put(idFn.apply(item), item);
            }
        }
    }

    public void addChangeListener(Runnable listener) {
        if (listener != null) {
            changeListeners.add(listener);
        }
    }

    private void notifyChanged() {
        for (Runnable listener : changeListeners) {
            listener.run();
        }
    }

    public synchronized List<Department> departments() { return copy(departments); }
    public synchronized List<Team> teams() { return copy(teams); }
    public synchronized List<Employee> employees() { return copy(employees); }
    public synchronized List<Product> products() { return copy(products); }
    public synchronized List<ProductVersion> versions() { return copy(versions); }
    public synchronized List<Project> projects() { return copy(projects); }
    public synchronized List<Task> tasks() { return copy(tasks); }
    public synchronized List<Issue> issues() { return copy(issues); }
    public synchronized List<Release> releases() { return copy(releases); }
    public synchronized List<Customer> customers() { return copy(customers); }
    public synchronized List<SalesLead> leads() { return copy(leads); }
    public synchronized List<SalesOpportunity> opportunities() { return copy(opportunities); }
    public synchronized List<SalesDeal> deals() { return copy(deals); }
    public synchronized List<AppNotification> notifications() { return copy(notifications); }
    public synchronized List<ActivityEntry> activities() { return copy(activities); }

    public synchronized Optional<Employee> employee(String id) { return Optional.ofNullable(employees.get(id)); }
    public synchronized Optional<Product> product(String id) { return Optional.ofNullable(products.get(id)); }
    public synchronized Optional<Project> project(String id) { return Optional.ofNullable(projects.get(id)); }
    public synchronized Optional<Task> task(String id) { return Optional.ofNullable(tasks.get(id)); }
    public synchronized Optional<Issue> issue(String id) { return Optional.ofNullable(issues.get(id)); }
    public synchronized Optional<Release> release(String id) { return Optional.ofNullable(releases.get(id)); }

    public synchronized Employee putEmployee(Employee e) { employees.put(e.getId(), e); notifyChanged(); return e; }
    public synchronized Product putProduct(Product p) { products.put(p.getId().getValue(), p); notifyChanged(); return p; }
    public synchronized Project putProject(Project p) { projects.put(p.getId(), p); notifyChanged(); return p; }
    public synchronized Task putTask(Task t) { tasks.put(t.getId(), t); notifyChanged(); return t; }
    public synchronized Issue putIssue(Issue i) { issues.put(i.getId(), i); notifyChanged(); return i; }
    public synchronized Release putRelease(Release r) { releases.put(r.getId(), r); notifyChanged(); return r; }
    public synchronized Department putDepartment(Department d) { departments.put(d.getId(), d); notifyChanged(); return d; }
    public synchronized Team putTeam(Team t) { teams.put(t.getId(), t); notifyChanged(); return t; }
    public synchronized ProductVersion putVersion(ProductVersion v) { versions.put(v.getId(), v); notifyChanged(); return v; }
    public synchronized Customer putCustomer(Customer c) { customers.put(c.getId(), c); notifyChanged(); return c; }
    public synchronized SalesLead putLead(SalesLead l) { leads.put(l.getId(), l); notifyChanged(); return l; }
    public synchronized SalesOpportunity putOpportunity(SalesOpportunity o) { opportunities.put(o.getId(), o); notifyChanged(); return o; }
    public synchronized SalesDeal putDeal(SalesDeal d) { deals.put(d.getId(), d); notifyChanged(); return d; }
    public synchronized AppNotification putNotification(AppNotification n) { notifications.put(n.getId(), n); notifyChanged(); return n; }
    public synchronized ActivityEntry putActivity(ActivityEntry a) { activities.put(a.getId(), a); notifyChanged(); return a; }

    public synchronized boolean removeEmployee(String id) { boolean r = employees.remove(id) != null; if (r) notifyChanged(); return r; }
    public synchronized boolean removeProduct(String id) { boolean r = products.remove(id) != null; if (r) notifyChanged(); return r; }
    public synchronized boolean removeProject(String id) { boolean r = projects.remove(id) != null; if (r) notifyChanged(); return r; }
    public synchronized boolean removeTask(String id) { boolean r = tasks.remove(id) != null; if (r) notifyChanged(); return r; }
    public synchronized boolean removeIssue(String id) { boolean r = issues.remove(id) != null; if (r) notifyChanged(); return r; }
    public synchronized boolean removeRelease(String id) { boolean r = releases.remove(id) != null; if (r) notifyChanged(); return r; }
    public synchronized boolean removeDepartment(String id) { boolean r = departments.remove(id) != null; if (r) notifyChanged(); return r; }
    public synchronized boolean removeTeam(String id) { boolean r = teams.remove(id) != null; if (r) notifyChanged(); return r; }
    public synchronized boolean removeVersion(String id) { boolean r = versions.remove(id) != null; if (r) notifyChanged(); return r; }
    public synchronized boolean removeCustomer(String id) { boolean r = customers.remove(id) != null; if (r) notifyChanged(); return r; }
    public synchronized boolean removeLead(String id) { boolean r = leads.remove(id) != null; if (r) notifyChanged(); return r; }
    public synchronized boolean removeOpportunity(String id) { boolean r = opportunities.remove(id) != null; if (r) notifyChanged(); return r; }
    public synchronized boolean removeDeal(String id) { boolean r = deals.remove(id) != null; if (r) notifyChanged(); return r; }
    public synchronized boolean removeNotification(String id) { boolean r = notifications.remove(id) != null; if (r) notifyChanged(); return r; }
    public synchronized boolean removeActivity(String id) { boolean r = activities.remove(id) != null; if (r) notifyChanged(); return r; }

    public synchronized Map<String, Integer> recordCounts() {
        Map<String, Integer> counts = new LinkedHashMap<>();
        counts.put("departments", departments.size());
        counts.put("teams", teams.size());
        counts.put("employees", employees.size());
        counts.put("products", products.size());
        counts.put("versions", versions.size());
        counts.put("projects", projects.size());
        counts.put("tasks", tasks.size());
        counts.put("issues", issues.size());
        counts.put("releases", releases.size());
        counts.put("customers", customers.size());
        counts.put("leads", leads.size());
        counts.put("opportunities", opportunities.size());
        counts.put("deals", deals.size());
        counts.put("notifications", notifications.size());
        counts.put("activities", activities.size());
        return Collections.unmodifiableMap(counts);
    }

    private static <T> List<T> copy(Map<String, T> map) {
        return new ArrayList<>(map.values());
    }
}
