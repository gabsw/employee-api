package com.tdx.employee.api.employee.resource;

import com.tdx.employee.api.employee.entity.Employee;
import com.tdx.employee.api.employee.entity.Title;
import com.tdx.employee.api.employee.model.EmployeeDTO;
import com.tdx.employee.api.employee.repository.EmployeeRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.time.LocalDate;
import java.util.List;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/api/employee")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployeeResource {
    @Context
    UriInfo uriInfo;

    @Inject
    EmployeeRepository repository;

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") final Long id) {
        return repository.findByIdOptional(id)
                         .map(Response::ok) // emp -> Response.ok(emp)
                         .orElse(Response.status(NOT_FOUND))
                         .build();
    }

    @GET
    @Path("/")
    public Response getByTitle(@QueryParam("title") final Title title) {
        final List<Employee> employees = repository.find("title", title).list();
        return Response.ok(employees).build();
    }

    /*@GET
    @Path("/")
    public Response getByStartDate(
        @QueryParam("from_date") final LocalDate fromDate,
        @QueryParam("to_date") final LocalDate toDate) {
        return Response.ok(repository.findBetweenDates(fromDate, toDate)).build();
    }*/


    @POST
    @Transactional
    @Path("/")
    public Response createEmployee(@Valid @NotNull final EmployeeDTO employeeDTO) {
        return repository.create(employeeDTO.toEmployee())
                         .map(emp -> uriInfo.getAbsolutePathBuilder()
                                            .path(Employee.class)
                                            .path(Employee.class, "getById")
                                            .build(emp.getId()))
                         .map(Response::created)
                         .orElse(Response.status(NOT_FOUND))
                         .build();
    }

    @PUT
    @Transactional
    @Path("/{id}")
    public Response updateEmployee(@PathParam("id") final Long id, @Valid @NotNull final EmployeeDTO toUpdate) {
        return repository.update(id, toUpdate.toEmployee())
                         .map(Response::ok)
                         .orElse(Response.status(NOT_FOUND))
                         .build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteEmployee(@PathParam("id") final Long id) {
//        Follows the Java reference guide
//        return repository.delete(id)
//            .map(emp -> Response.noContent())
//            .orElse(Response.status(Response.Status.NOT_FOUND))
//            .build();
        boolean deleted = repository.deleteById(id);
        return (deleted ? Response.noContent() : Response.status(NOT_FOUND)).build();
    }
}
