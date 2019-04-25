package edu.socialnetwork.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Ethnicity entity. This class is used in EthnicityResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /ethnicities?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EthnicityCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter ethnicity;

    private LongFilter userId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(StringFilter ethnicity) {
        this.ethnicity = ethnicity;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EthnicityCriteria that = (EthnicityCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(ethnicity, that.ethnicity) &&
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        ethnicity,
        userId
        );
    }

    @Override
    public String toString() {
        return "EthnicityCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (ethnicity != null ? "ethnicity=" + ethnicity + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
