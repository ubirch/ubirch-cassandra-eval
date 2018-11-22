# Cassandra Configuration

# Contact points for your cluster, currently only the first is used
contact_points = ['127.0.0.1']

# Keyspace to work with, this doesn't have to exist yet.
keyspace = 'trireme_test'
# Replication options. Defined as a map just as you would in CQL.
replication = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 }
# replication = {'class' : 'NetworkTopologyStrategy', 'dc1' : 3, 'dc2' : 2}

# Authentication Information
username = None
password = None

# Flag indicating whether this host is the migration master. Migrations are only run on the migration master
migration_master = True


# Solr Configuration
solr_url = None