/* Benchmarking Suite
   Copyright 2018 Engineering Ingegneria Informatica S.p.A.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

   Developed in the ARTIST EU project (www.artist-project.eu) and in the
   CloudPerfect EU project (https://cloudperfect.eu/)
*/
package org.benchsuite.qoehelper.model;

public class HardwareProfile {

  private String id;
  private String name;
  private int ram;
  private double vcpus;
  private String displayName;

  public HardwareProfile() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
    this.refreshDisplayName();
  }

  public int getRam() {
    return ram;
  }

  public void setRam(int ram) {
    this.ram = ram;
    this.refreshDisplayName();
  }

  public double getVcpus() {
    return vcpus;
  }

  public void setVcpus(double d) {
    this.vcpus = d;
    this.refreshDisplayName();
  }

  public String getDisplayName() {
    return displayName;
  }

  private void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  private void refreshDisplayName(){
    this.displayName = this.name + " - " + (int)Math.round(vcpus) + " CPUs, " + this.ram + " MBs RAM";
  }
}
