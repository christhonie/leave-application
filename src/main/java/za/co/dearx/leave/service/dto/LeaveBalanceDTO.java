package za.co.dearx.leave.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import javax.annotation.Generated;
import za.co.dearx.leave.domain.LeaveType;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "type", "total", "pending" })
@Generated("jsonschema2pojo")
public class LeaveBalanceDTO implements Serializable {

    private static final long serialVersionUID = 3280543622034509644L;

    @JsonProperty("type")
    private LeaveType type;

    @JsonProperty("total")
    private long total;

    @JsonProperty("pending")
    private long pending;

    @JsonProperty("type")
    public LeaveType getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(LeaveType type) {
        this.type = type;
    }

    public LeaveBalanceDTO withType(LeaveType type) {
        this.type = type;
        return this;
    }

    @JsonProperty("total")
    public long getTotal() {
        return total;
    }

    @JsonProperty("total")
    public void setTotal(long total) {
        this.total = total;
    }

    public LeaveBalanceDTO withTotal(long total) {
        this.total = total;
        return this;
    }

    @JsonProperty("pending")
    public long getPending() {
        return pending;
    }

    @JsonProperty("pending")
    public void setPending(long pending) {
        this.pending = pending;
    }

    public LeaveBalanceDTO withPending(long pending) {
        this.pending = pending;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(LeaveBalanceDTO.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null) ? "<null>" : this.type));
        sb.append(',');
        sb.append("total");
        sb.append('=');
        sb.append(this.total);
        sb.append(',');
        sb.append("pending");
        sb.append('=');
        sb.append(this.pending);
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }
}
