/**
 * 
 */
package org.jrpc.registry;

import java.io.Serializable;


/**
 * @author caoguo(jiwei.guo)
 *
 */
public class RegisterMeta implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Address address = new Address();
    
	private ServiceMeta serviceMeta = new ServiceMeta();
    
	public String getHost() {
		return address.getHost();
	}
	public void setHost(String host) {
		address.setHost(host);
	}
	public int getPort() {
		return address.getPort();
	}
	public void setPort(int port) {
		address.setPort(port);
	}
	public String getGroup() {
		return serviceMeta.getGroup();
	}
	public void setGroup(String group) {
		serviceMeta.setGroup(group);
	}
	public String getVersion() {
		return serviceMeta.getVersion();
	}
	public void setVersion(String version) {
		serviceMeta.setVersion(version);
	}
	public String getServiceProviderName() {
		return serviceMeta.getServiceProviderName();
	}
	public void setServiceProviderName(String serviceProviderName) {
		serviceMeta.setServiceProviderName(serviceProviderName);
	} 
	
	public Address getAddress() {
		return address;
	}
	public ServiceMeta getServiceMeta() {
		return serviceMeta;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((serviceMeta == null) ? 0 : serviceMeta.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegisterMeta other = (RegisterMeta) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (serviceMeta == null) {
			if (other.serviceMeta != null)
				return false;
		} else if (!serviceMeta.equals(other.serviceMeta))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RegisterMeta [address=" + address + ", serviceMeta=" + serviceMeta + "]";
	}





	public class Address implements Serializable{

		private static final long serialVersionUID = 1L;
		private String host;
		private int port;

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((host == null) ? 0 : host.hashCode());
			result = prime * result + port;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Address other = (Address) obj;
			if (host == null) {
				if (other.host != null)
					return false;
			} else if (!host.equals(other.host))
				return false;
			if (port != other.port)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Address [host=" + host + ", port=" + port + "]";
		}

	}
	
	public class ServiceMeta implements Serializable{
		private static final long serialVersionUID = 1L;
		private String group;
		private String version;
		private String serviceProviderName;
		public String getGroup() {
			return group;
		}
		public void setGroup(String group) {
			this.group = group;
		}
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}
		public String getServiceProviderName() {
			return serviceProviderName;
		}
		public void setServiceProviderName(String serviceProviderName) {
			this.serviceProviderName = serviceProviderName;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((group == null) ? 0 : group.hashCode());
			result = prime * result + ((serviceProviderName == null) ? 0 : serviceProviderName.hashCode());
			result = prime * result + ((version == null) ? 0 : version.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ServiceMeta other = (ServiceMeta) obj;
			if (group == null) {
				if (other.group != null)
					return false;
			} else if (!group.equals(other.group))
				return false;
			if (serviceProviderName == null) {
				if (other.serviceProviderName != null)
					return false;
			} else if (!serviceProviderName.equals(other.serviceProviderName))
				return false;
			if (version == null) {
				if (other.version != null)
					return false;
			} else if (!version.equals(other.version))
				return false;
			return true;
		}
		@Override
		public String toString() {
			return "ServiceMeta [group=" + group + ", version=" + version + ", serviceProviderName="
					+ serviceProviderName + "]";
		}
		
	}
}
