package com.festember16.app; /**
 * Created by vishnu on 5/8/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;


public class EventResponse {

    @SerializedName("status")
    @Expose
    private long status;
    @SerializedName("data")
    @Expose
    private List<Events> data = new ArrayList<Events>();

    /**
     *
     * @return
     * The status
     */
    public long getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(long status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The data
     */
    public List<Events> getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(List<Events> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(status).append(data).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof EventResponse) == false) {
            return false;
        }
        EventResponse rhs = ((EventResponse) other);
        return new EqualsBuilder().append(status, rhs.status).append(data, rhs.data).isEquals();
    }

}