//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.web;

import com.inthergroup.webpoc.framework.domain.*;
import com.inthergroup.webpoc.framework.services.EntityService;
import com.inthergroup.webpoc.framework.services.SecurityGroupService;
import com.inthergroup.webpoc.framework.web.errors.FrameworkEntityNotFoundException;
import lombok.Data;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @author gvandenbekerom
 * @since 19-Sep-18
 *
 * Used for basic entity rest functions and 'global' exception handling
 */
abstract public class BaseFrameworkController<T extends BaseEntity, TID, TREPO extends CrudRepository<T, TID>, TSERVICE extends EntityService<T, TID, TREPO>> {

    final TSERVICE service;
    private Class<T> entityClass;

    @Autowired
    private SecurityGroupService securityGroupService;

    @SuppressWarnings("unchecked") // When used properly this will never be a problem.
    BaseFrameworkController(TSERVICE service) {
        this.service = service;
        // The following line uses some reflection to find the type of the first generic type
        // Without this a user has to pass the type as both a generic type and a constructor parameter.
        this.entityClass = (Class<T>)
                ((ParameterizedType)getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    @GetMapping
    public Iterable<T> getAll() {
        return service.findAll();
    }

    @GetMapping("{id}")
    public T getById(@PathVariable TID id) {
        return findById(id);
    }

    @PostMapping
    public T create(@Valid @RequestBody T obj) {
        return service.save(obj);
    }

    /**
     * Use this function for the default http PUT functionality. make sure to annotate overridden function with @PutMapping("{id}")
     * If for some reason you don't want this functionality in your controller, just return null and dont add the @PutMapping annotation
     *
     * @param newObj new values for the entity should have @Valid and @RequestBody
     * @param id of the entity to update should have @PathVariable
     * @return updated object from jpa context
     */
    abstract public T update(@Valid @RequestBody T newObj, @PathVariable TID id);

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable TID id) {
        T obj = findById(id);
        service.delete(obj);
    }

    @PreAuthorize("hasRole('DEV') or hasRole('ADMINISTRATORS')")
    @PutMapping("/{id}/allowedUserGroups")
    public T setAllowedUserGroups(@RequestBody List<String> userGroups, @PathVariable TID id) {
        T obj = findById(id);
        List<SecurityGroup> securityGroups = securityGroupService.generateFromIdStrings(userGroups);
        obj.setSecurityGroups(securityGroups);

        if (obj instanceof Overview) {
            ((Overview) obj).getColumns().forEach(c -> c.setSecurityGroups(securityGroups));
        }
        else if (obj instanceof MenuItem) {
            Action action = ((MenuItem) obj).getAction();
            if (action != null) action.setSecurityGroups(securityGroups);
        }
        
        service.save(obj);
        return obj;
    }

    T findById(TID id) {
        return service.findById(id).orElseThrow(() -> new FrameworkEntityNotFoundException(entityClass, id));
    }

    /*--------------------------------------*/
    /*  Exception Handling                  */
    /*--------------------------------------*/
    private @Data class ErrorMessage {
        private final String message;
    }

    /**
     * Handles hibernate constraint exceptions
     * @return ErrorMessage
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage hibernateConstraintViolationHandler() {
        return new ErrorMessage("Operation violates a relation of the given object");
    }

    /**
     * Handles java validation constraint exceptions
     * @return ErrorMessage
     */
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage validationConstraintViolationHandler(javax.validation.ConstraintViolationException e) {
        return new ErrorMessage(e.getMessage());
    }

    /**
     * Handles http body -> pojo conversion exceptions
     * @return ErrorMessage
     */
    @ExceptionHandler(HttpMessageConversionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage httpMessageConversionExceptionHandler() {
        return new ErrorMessage("Unable to convert request body to expected entity type");
    }
}
