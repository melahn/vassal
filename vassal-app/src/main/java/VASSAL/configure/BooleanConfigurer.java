/*
 *
 * Copyright (c) 2000-2003 by Rodney Kinney
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License (LGPL) as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, copies are available
 * at http://www.opensource.org.
 */
package VASSAL.configure;

import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 * Configurer for Boolean values
 */
public class BooleanConfigurer extends Configurer {
  protected JPanel p;
  private javax.swing.JCheckBox box;

  public BooleanConfigurer(String key, String name, Boolean val) {
    super(key, name, val);
  }

  public BooleanConfigurer(String key, String name, boolean val) {
    super(key, name, val ? Boolean.TRUE : Boolean.FALSE);
  }

  public BooleanConfigurer(String key, String name) {
    this(key, name, Boolean.FALSE);
  }

  public BooleanConfigurer (Boolean val) {
    this(null, null, val);
  }

  public BooleanConfigurer (boolean val) {
    this(null, null, val);
  }

  @Override
  public String getValueString() {
    return booleanValue().toString();
  }

  public boolean getValueBoolean() {
    return booleanValue();
  }

  @Override
  public void setValue(Object o) {
    super.setValue(o);
    if (box != null && !Boolean.valueOf(box.isSelected()).equals(o)) {
      box.setSelected(booleanValue());
    }
  }

  @Override
  public void setValue(String s) {
    setValue(Boolean.valueOf(s));
  }

  @Override
  public void setName(String s) {
    super.setName(s);
    if (box != null) {
      box.setText(s);
    }
  }

  @Override
  public java.awt.Component getControls() {
    if (p == null) {
      if (getName() == null || getName().isEmpty()) {
        p = new JPanel(new MigLayout("ins 0", "[fill,grow]0[0]")); // NON-NLS
      }
      else {
        p = new JPanel(new MigLayout("ins 0", "[][fill,grow][]")); // NON-NLS
        p.add(new JLabel(getName()));
      }
      box = new ConfigurerCheckBox();
      box.setSelected(booleanValue());
      box.addItemListener(e -> setValue(box.isSelected()));
      p.add(box);
    }

    return p;
  }

  public Boolean booleanValue() {
    return (Boolean) value;
  }
}
