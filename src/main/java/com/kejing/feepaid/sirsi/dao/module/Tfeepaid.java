package com.kejing.feepaid.sirsi.dao.module;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Tfeepaid entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tfeepaid", schema = "public")

public class Tfeepaid implements java.io.Serializable {

	// Fields

	private Long id;
	private String fcertid;
	private Timestamp ftimestamp;
	private String ffeetype;
	private String fpaymenttype;
	private String fcurrencytype;
	private Double ffeeamount;
	private String ffeeidentifier;
	private Long fdevid;
	private Long fstatus;
	private Long fopercode;
	private String fremark;
	private String forderid;
	private Timestamp flastcommit;
	private Integer fcommittimes;
	private String fcashier;
	private String fcardno;
	private String fyktls;
	private String fresv1;

	// Constructors

	/** default constructor */
	public Tfeepaid() {
	}

	/** minimal constructor */
	public Tfeepaid(Long id, String fcertid, Timestamp ftimestamp, String ffeetype, String fcurrencytype,
			Double ffeeamount, Long fstatus, Long fopercode) {
		this.id = id;
		this.fcertid = fcertid;
		this.ftimestamp = ftimestamp;
		this.ffeetype = ffeetype;
		this.fcurrencytype = fcurrencytype;
		this.ffeeamount = ffeeamount;
		this.fstatus = fstatus;
		this.fopercode = fopercode;
	}

	/** full constructor */
	public Tfeepaid(Long id, String fcertid, Timestamp ftimestamp, String ffeetype, String fpaymenttype,
			String fcurrencytype, Double ffeeamount, String ffeeidentifier, Long fdevid, Long fstatus, Long fopercode,
			String fremark, String forderid, Timestamp flastcommit, Integer fcommittimes, String fcashier,
			String fcardno, String fyktls, String fresv1) {
		this.id = id;
		this.fcertid = fcertid;
		this.ftimestamp = ftimestamp;
		this.ffeetype = ffeetype;
		this.fpaymenttype = fpaymenttype;
		this.fcurrencytype = fcurrencytype;
		this.ffeeamount = ffeeamount;
		this.ffeeidentifier = ffeeidentifier;
		this.fdevid = fdevid;
		this.fstatus = fstatus;
		this.fopercode = fopercode;
		this.fremark = fremark;
		this.forderid = forderid;
		this.flastcommit = flastcommit;
		this.fcommittimes = fcommittimes;
		this.fcashier = fcashier;
		this.fcardno = fcardno;
		this.fyktls = fyktls;
		this.fresv1 = fresv1;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name = "generator", sequenceName = "seq_id", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "fcertid", nullable = false)

	public String getFcertid() {
		return this.fcertid;
	}

	public void setFcertid(String fcertid) {
		this.fcertid = fcertid;
	}

	@Column(name = "ftimestamp", nullable = false, length = 29)

	public Timestamp getFtimestamp() {
		return this.ftimestamp;
	}

	public void setFtimestamp(Timestamp ftimestamp) {
		this.ftimestamp = ftimestamp;
	}

	@Column(name = "ffeetype", nullable = false)

	public String getFfeetype() {
		return this.ffeetype;
	}

	public void setFfeetype(String ffeetype) {
		this.ffeetype = ffeetype;
	}

	@Column(name = "fpaymenttype")

	public String getFpaymenttype() {
		return this.fpaymenttype;
	}

	public void setFpaymenttype(String fpaymenttype) {
		this.fpaymenttype = fpaymenttype;
	}

	@Column(name = "fcurrencytype", nullable = false)

	public String getFcurrencytype() {
		return this.fcurrencytype;
	}

	public void setFcurrencytype(String fcurrencytype) {
		this.fcurrencytype = fcurrencytype;
	}

	@Column(name = "ffeeamount", nullable = false, precision = 8)

	public Double getFfeeamount() {
		return this.ffeeamount;
	}

	public void setFfeeamount(Double ffeeamount) {
		this.ffeeamount = ffeeamount;
	}

	@Column(name = "ffeeidentifier")

	public String getFfeeidentifier() {
		return this.ffeeidentifier;
	}

	public void setFfeeidentifier(String ffeeidentifier) {
		this.ffeeidentifier = ffeeidentifier;
	}

	@Column(name = "fdevid")

	public Long getFdevid() {
		return this.fdevid;
	}

	public void setFdevid(Long fdevid) {
		this.fdevid = fdevid;
	}

	@Column(name = "fstatus", nullable = false)

	public Long getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Long fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fopercode", nullable = false)

	public Long getFopercode() {
		return this.fopercode;
	}

	public void setFopercode(Long fopercode) {
		this.fopercode = fopercode;
	}

	@Column(name = "fremark")

	public String getFremark() {
		return this.fremark;
	}

	public void setFremark(String fremark) {
		this.fremark = fremark;
	}

	@Column(name = "forderid")

	public String getForderid() {
		return this.forderid;
	}

	public void setForderid(String forderid) {
		this.forderid = forderid;
	}

	@Column(name = "flastcommit", length = 29)

	public Timestamp getFlastcommit() {
		return this.flastcommit;
	}

	public void setFlastcommit(Timestamp flastcommit) {
		this.flastcommit = flastcommit;
	}

	@Column(name = "fcommittimes")

	public Integer getFcommittimes() {
		return this.fcommittimes;
	}

	public void setFcommittimes(Integer fcommittimes) {
		this.fcommittimes = fcommittimes;
	}

	@Column(name = "fcashier")

	public String getFcashier() {
		return this.fcashier;
	}

	public void setFcashier(String fcashier) {
		this.fcashier = fcashier;
	}

	@Column(name = "fcardno")

	public String getFcardno() {
		return this.fcardno;
	}

	public void setFcardno(String fcardno) {
		this.fcardno = fcardno;
	}

	@Column(name = "fyktls")

	public String getFyktls() {
		return this.fyktls;
	}

	public void setFyktls(String fyktls) {
		this.fyktls = fyktls;
	}

	@Column(name = "fresv1")

	public String getFresv1() {
		return this.fresv1;
	}

	public void setFresv1(String fresv1) {
		this.fresv1 = fresv1;
	}

}