/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.alibaba.dts.formats.avro;

import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.SchemaStore;
import org.apache.avro.specific.SpecificData;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class DateTime extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -1040122710886440465L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"DateTime\",\"namespace\":\"com.alibaba.dts.formats.avro\",\"fields\":[{\"name\":\"year\",\"type\":[\"null\",\"int\"],\"default\":null},{\"name\":\"month\",\"type\":[\"null\",\"int\"],\"default\":null},{\"name\":\"day\",\"type\":[\"null\",\"int\"],\"default\":null},{\"name\":\"hour\",\"type\":[\"null\",\"int\"],\"default\":null},{\"name\":\"minute\",\"type\":[\"null\",\"int\"],\"default\":null},{\"name\":\"second\",\"type\":[\"null\",\"int\"],\"default\":null},{\"name\":\"millis\",\"type\":[\"null\",\"int\"],\"default\":null}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<DateTime> ENCODER =
          new BinaryMessageEncoder<DateTime>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<DateTime> DECODER =
          new BinaryMessageDecoder<DateTime>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   */
  public static BinaryMessageDecoder<DateTime> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   */
  public static BinaryMessageDecoder<DateTime> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<DateTime>(MODEL$, SCHEMA$, resolver);
  }

  /** Serializes this DateTime to a ByteBuffer. */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /** Deserializes a DateTime from a ByteBuffer. */
  public static DateTime fromByteBuffer(
          java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  @Deprecated public java.lang.Integer year;
  @Deprecated public java.lang.Integer month;
  @Deprecated public java.lang.Integer day;
  @Deprecated public java.lang.Integer hour;
  @Deprecated public java.lang.Integer minute;
  @Deprecated public java.lang.Integer second;
  @Deprecated public java.lang.Integer millis;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public DateTime() {}

  /**
   * All-args constructor.
   * @param year The new value for year
   * @param month The new value for month
   * @param day The new value for day
   * @param hour The new value for hour
   * @param minute The new value for minute
   * @param second The new value for second
   * @param millis The new value for millis
   */
  public DateTime(java.lang.Integer year, java.lang.Integer month, java.lang.Integer day, java.lang.Integer hour, java.lang.Integer minute, java.lang.Integer second, java.lang.Integer millis) {
    this.year = year;
    this.month = month;
    this.day = day;
    this.hour = hour;
    this.minute = minute;
    this.second = second;
    this.millis = millis;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public Object get(int field$) {
    switch (field$) {
      case 0: return year;
      case 1: return month;
      case 2: return day;
      case 3: return hour;
      case 4: return minute;
      case 5: return second;
      case 6: return millis;
      default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, Object value$) {
    switch (field$) {
      case 0: year = (java.lang.Integer)value$; break;
      case 1: month = (java.lang.Integer)value$; break;
      case 2: day = (java.lang.Integer)value$; break;
      case 3: hour = (java.lang.Integer)value$; break;
      case 4: minute = (java.lang.Integer)value$; break;
      case 5: second = (java.lang.Integer)value$; break;
      case 6: millis = (java.lang.Integer)value$; break;
      default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'year' field.
   * @return The value of the 'year' field.
   */
  public java.lang.Integer getYear() {
    return year;
  }

  /**
   * Sets the value of the 'year' field.
   * @param value the value to set.
   */
  public void setYear(java.lang.Integer value) {
    this.year = value;
  }

  /**
   * Gets the value of the 'month' field.
   * @return The value of the 'month' field.
   */
  public java.lang.Integer getMonth() {
    return month;
  }

  /**
   * Sets the value of the 'month' field.
   * @param value the value to set.
   */
  public void setMonth(java.lang.Integer value) {
    this.month = value;
  }

  /**
   * Gets the value of the 'day' field.
   * @return The value of the 'day' field.
   */
  public java.lang.Integer getDay() {
    return day;
  }

  /**
   * Sets the value of the 'day' field.
   * @param value the value to set.
   */
  public void setDay(java.lang.Integer value) {
    this.day = value;
  }

  /**
   * Gets the value of the 'hour' field.
   * @return The value of the 'hour' field.
   */
  public java.lang.Integer getHour() {
    return hour;
  }

  /**
   * Sets the value of the 'hour' field.
   * @param value the value to set.
   */
  public void setHour(java.lang.Integer value) {
    this.hour = value;
  }

  /**
   * Gets the value of the 'minute' field.
   * @return The value of the 'minute' field.
   */
  public java.lang.Integer getMinute() {
    return minute;
  }

  /**
   * Sets the value of the 'minute' field.
   * @param value the value to set.
   */
  public void setMinute(java.lang.Integer value) {
    this.minute = value;
  }

  /**
   * Gets the value of the 'second' field.
   * @return The value of the 'second' field.
   */
  public java.lang.Integer getSecond() {
    return second;
  }

  /**
   * Sets the value of the 'second' field.
   * @param value the value to set.
   */
  public void setSecond(java.lang.Integer value) {
    this.second = value;
  }

  /**
   * Gets the value of the 'millis' field.
   * @return The value of the 'millis' field.
   */
  public java.lang.Integer getMillis() {
    return millis;
  }

  /**
   * Sets the value of the 'millis' field.
   * @param value the value to set.
   */
  public void setMillis(java.lang.Integer value) {
    this.millis = value;
  }

  /**
   * Creates a new DateTime RecordBuilder.
   * @return A new DateTime RecordBuilder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Creates a new DateTime RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new DateTime RecordBuilder
   */
  public static Builder newBuilder(Builder other) {
    return new Builder(other);
  }

  /**
   * Creates a new DateTime RecordBuilder by copying an existing DateTime instance.
   * @param other The existing instance to copy.
   * @return A new DateTime RecordBuilder
   */
  public static Builder newBuilder(DateTime other) {
    return new Builder(other);
  }

  /**
   * RecordBuilder for DateTime instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<DateTime>
          implements org.apache.avro.data.RecordBuilder<DateTime> {

    private java.lang.Integer year;
    private java.lang.Integer month;
    private java.lang.Integer day;
    private java.lang.Integer hour;
    private java.lang.Integer minute;
    private java.lang.Integer second;
    private java.lang.Integer millis;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.year)) {
        this.year = data().deepCopy(fields()[0].schema(), other.year);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.month)) {
        this.month = data().deepCopy(fields()[1].schema(), other.month);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.day)) {
        this.day = data().deepCopy(fields()[2].schema(), other.day);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.hour)) {
        this.hour = data().deepCopy(fields()[3].schema(), other.hour);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.minute)) {
        this.minute = data().deepCopy(fields()[4].schema(), other.minute);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.second)) {
        this.second = data().deepCopy(fields()[5].schema(), other.second);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.millis)) {
        this.millis = data().deepCopy(fields()[6].schema(), other.millis);
        fieldSetFlags()[6] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing DateTime instance
     * @param other The existing instance to copy.
     */
    private Builder(DateTime other) {
      super(SCHEMA$);
      if (isValidValue(fields()[0], other.year)) {
        this.year = data().deepCopy(fields()[0].schema(), other.year);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.month)) {
        this.month = data().deepCopy(fields()[1].schema(), other.month);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.day)) {
        this.day = data().deepCopy(fields()[2].schema(), other.day);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.hour)) {
        this.hour = data().deepCopy(fields()[3].schema(), other.hour);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.minute)) {
        this.minute = data().deepCopy(fields()[4].schema(), other.minute);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.second)) {
        this.second = data().deepCopy(fields()[5].schema(), other.second);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.millis)) {
        this.millis = data().deepCopy(fields()[6].schema(), other.millis);
        fieldSetFlags()[6] = true;
      }
    }

    /**
     * Gets the value of the 'year' field.
     * @return The value.
     */
    public java.lang.Integer getYear() {
      return year;
    }

    /**
     * Sets the value of the 'year' field.
     * @param value The value of 'year'.
     * @return This builder.
     */
    public Builder setYear(java.lang.Integer value) {
      validate(fields()[0], value);
      this.year = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
     * Checks whether the 'year' field has been set.
     * @return True if the 'year' field has been set, false otherwise.
     */
    public boolean hasYear() {
      return fieldSetFlags()[0];
    }


    /**
     * Clears the value of the 'year' field.
     * @return This builder.
     */
    public Builder clearYear() {
      year = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
     * Gets the value of the 'month' field.
     * @return The value.
     */
    public java.lang.Integer getMonth() {
      return month;
    }

    /**
     * Sets the value of the 'month' field.
     * @param value The value of 'month'.
     * @return This builder.
     */
    public Builder setMonth(java.lang.Integer value) {
      validate(fields()[1], value);
      this.month = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
     * Checks whether the 'month' field has been set.
     * @return True if the 'month' field has been set, false otherwise.
     */
    public boolean hasMonth() {
      return fieldSetFlags()[1];
    }


    /**
     * Clears the value of the 'month' field.
     * @return This builder.
     */
    public Builder clearMonth() {
      month = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
     * Gets the value of the 'day' field.
     * @return The value.
     */
    public java.lang.Integer getDay() {
      return day;
    }

    /**
     * Sets the value of the 'day' field.
     * @param value The value of 'day'.
     * @return This builder.
     */
    public Builder setDay(java.lang.Integer value) {
      validate(fields()[2], value);
      this.day = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
     * Checks whether the 'day' field has been set.
     * @return True if the 'day' field has been set, false otherwise.
     */
    public boolean hasDay() {
      return fieldSetFlags()[2];
    }


    /**
     * Clears the value of the 'day' field.
     * @return This builder.
     */
    public Builder clearDay() {
      day = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
     * Gets the value of the 'hour' field.
     * @return The value.
     */
    public java.lang.Integer getHour() {
      return hour;
    }

    /**
     * Sets the value of the 'hour' field.
     * @param value The value of 'hour'.
     * @return This builder.
     */
    public Builder setHour(java.lang.Integer value) {
      validate(fields()[3], value);
      this.hour = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
     * Checks whether the 'hour' field has been set.
     * @return True if the 'hour' field has been set, false otherwise.
     */
    public boolean hasHour() {
      return fieldSetFlags()[3];
    }


    /**
     * Clears the value of the 'hour' field.
     * @return This builder.
     */
    public Builder clearHour() {
      hour = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
     * Gets the value of the 'minute' field.
     * @return The value.
     */
    public java.lang.Integer getMinute() {
      return minute;
    }

    /**
     * Sets the value of the 'minute' field.
     * @param value The value of 'minute'.
     * @return This builder.
     */
    public Builder setMinute(java.lang.Integer value) {
      validate(fields()[4], value);
      this.minute = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
     * Checks whether the 'minute' field has been set.
     * @return True if the 'minute' field has been set, false otherwise.
     */
    public boolean hasMinute() {
      return fieldSetFlags()[4];
    }


    /**
     * Clears the value of the 'minute' field.
     * @return This builder.
     */
    public Builder clearMinute() {
      minute = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    /**
     * Gets the value of the 'second' field.
     * @return The value.
     */
    public java.lang.Integer getSecond() {
      return second;
    }

    /**
     * Sets the value of the 'second' field.
     * @param value The value of 'second'.
     * @return This builder.
     */
    public Builder setSecond(java.lang.Integer value) {
      validate(fields()[5], value);
      this.second = value;
      fieldSetFlags()[5] = true;
      return this;
    }

    /**
     * Checks whether the 'second' field has been set.
     * @return True if the 'second' field has been set, false otherwise.
     */
    public boolean hasSecond() {
      return fieldSetFlags()[5];
    }


    /**
     * Clears the value of the 'second' field.
     * @return This builder.
     */
    public Builder clearSecond() {
      second = null;
      fieldSetFlags()[5] = false;
      return this;
    }

    /**
     * Gets the value of the 'millis' field.
     * @return The value.
     */
    public java.lang.Integer getMillis() {
      return millis;
    }

    /**
     * Sets the value of the 'millis' field.
     * @param value The value of 'millis'.
     * @return This builder.
     */
    public Builder setMillis(java.lang.Integer value) {
      validate(fields()[6], value);
      this.millis = value;
      fieldSetFlags()[6] = true;
      return this;
    }

    /**
     * Checks whether the 'millis' field has been set.
     * @return True if the 'millis' field has been set, false otherwise.
     */
    public boolean hasMillis() {
      return fieldSetFlags()[6];
    }


    /**
     * Clears the value of the 'millis' field.
     * @return This builder.
     */
    public Builder clearMillis() {
      millis = null;
      fieldSetFlags()[6] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DateTime build() {
      try {
        DateTime record = new DateTime();
        record.year = fieldSetFlags()[0] ? this.year : (java.lang.Integer) defaultValue(fields()[0]);
        record.month = fieldSetFlags()[1] ? this.month : (java.lang.Integer) defaultValue(fields()[1]);
        record.day = fieldSetFlags()[2] ? this.day : (java.lang.Integer) defaultValue(fields()[2]);
        record.hour = fieldSetFlags()[3] ? this.hour : (java.lang.Integer) defaultValue(fields()[3]);
        record.minute = fieldSetFlags()[4] ? this.minute : (java.lang.Integer) defaultValue(fields()[4]);
        record.second = fieldSetFlags()[5] ? this.second : (java.lang.Integer) defaultValue(fields()[5]);
        record.millis = fieldSetFlags()[6] ? this.millis : (java.lang.Integer) defaultValue(fields()[6]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<DateTime>
          WRITER$ = (org.apache.avro.io.DatumWriter<DateTime>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
          throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<DateTime>
          READER$ = (org.apache.avro.io.DatumReader<DateTime>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
          throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}
