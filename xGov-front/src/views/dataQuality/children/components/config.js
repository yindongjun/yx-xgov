export default {
  MySQL: [
    {
      label: 'IP',
      prop: 'ip',
      value: '',
      type: 'input',
      placeholder: '请输入IP'
    },
    {
      label: '端口',
      prop: 'post',
      value: '',
      type: 'input',
      placeholder: '默认端口3306'
    },
    {
      label: '数据库用户名',
      prop: 'userName',
      value: '',
      type: 'input',
      placeholder: '请输入数据库用户名'
    },
    {
      label: '数据库密码',
      prop: 'password',
      value: '',
      type: 'password',
      placeholder: '请输入数据库密码'
    },
    {
      label: '数据库名',
      prop: 'dbname',
      value: '',
      type: 'input',
      placeholder: '请输入数据库名'
    },
    {
      label: '字符集',
      prop: 'characterSet',
      value: 'UTF-8',
      type: 'select',
      options: [
        {
          value: 'UTF-8',
          label: 'UTF-8'
        },
        {
          value: 'GBK',
          label: 'GBK'
        }
      ],
      placeholder: '请选择字符集'
    },
    {
      label: 'URL连接参数',
      prop: 'urlConfig',
      value: '',
      type: 'input',
      isWidth: true,
      placeholder: '请输入URL连接参数'
    }
  ],
  Oracle: [
    {
      label: 'IP',
      prop: 'ip',
      value: '',
      type: 'input',
      placeholder: '请输入IP'
    },
    {
      label: '端口',
      prop: 'post',
      value: '',
      type: 'input',
      placeholder: '默认端口1521'
    },
    {
      label: '数据库用户名',
      prop: 'userName',
      value: '',
      type: 'input',
      placeholder: '请输入数据库用户名'
    },
    {
      label: '数据库密码',
      prop: 'password',
      value: '',
      type: 'password',
      placeholder: '请输入数据库密码'
    },
    {
      label: '数据库SID',
      prop: 'dbname',
      value: '',
      type: 'input',
      placeholder: '请输入数据库SID'
    },
    {
      label: 'Schema',
      prop: 'schemasName',
      value: '',
      type: 'input',
      placeholder: '请输入Schema'
    }
  ],
  SqlServer: [
    {
      label: 'IP',
      prop: 'ip',
      value: '',
      type: 'input',
      placeholder: '请输入IP'
    },
    {
      label: '端口',
      prop: 'post',
      value: '',
      type: 'input',
      placeholder: '默认端口1433'
    },
    {
      label: '数据库用户名',
      prop: 'userName',
      value: '',
      type: 'input',
      placeholder: '请输入数据库用户名'
    },
    {
      label: '数据库密码',
      prop: 'password',
      value: '',
      type: 'password',
      placeholder: '请输入数据库密码'
    },
    {
      label: '数据库名',
      prop: 'dbname',
      value: '',
      type: 'input',
      placeholder: '请输入数据库名'
    },
    {
      label: 'Schema',
      prop: 'schemasName',
      value: '',
      type: 'input',
      placeholder: '请输入Schema'
    }
  ],
  DB2: [
    {
      label: 'IP',
      prop: 'ip',
      value: '',
      type: 'input',
      placeholder: '请输入IP'
    },
    {
      label: '端口',
      prop: 'post',
      value: '',
      type: 'input',
      placeholder: '默认端口50000'
    },
    {
      label: '数据库用户名',
      prop: 'userName',
      value: '',
      type: 'input',
      placeholder: '请输入数据库用户名'
    },
    {
      label: '数据库密码',
      prop: 'password',
      value: '',
      type: 'password',
      placeholder: '请输入数据库密码'
    },
    {
      label: '数据库名称',
      prop: 'dbname',
      value: '',
      type: 'input',
      placeholder: '请输入数据库名称'
    },
    {
      label: '字符集',
      prop: 'characterSet',
      value: 'UTF-8',
      type: 'select',
      options: [
        {
          value: 'UTF-8',
          label: 'UTF-8'
        },
        {
          value: 'GBK',
          label: 'GBK'
        }
      ],
      placeholder: '请选择字符集'
    },
    {
      label: 'Schema',
      prop: 'schemasName',
      value: '',
      type: 'input',
      isWidth: true,
      placeholder: '请输入Schema'
    }
  ],
  PostgreSQL: [
    {
      label: 'IP',
      prop: 'ip',
      value: '',
      type: 'input',
      placeholder: '请输入IP'
    },
    {
      label: '端口',
      prop: 'post',
      value: '',
      type: 'input',
      placeholder: '默认端口5432'
    },
    {
      label: '数据库用户名',
      prop: 'userName',
      value: '',
      type: 'input',
      placeholder: '请输入数据库用户名'
    },
    {
      label: '数据库密码',
      prop: 'password',
      value: '',
      type: 'password',
      placeholder: '请输入数据库密码'
    },
    {
      label: '数据库名称',
      prop: 'dbname',
      value: '',
      type: 'input',
      placeholder: '请输入数据库名称'
    },
    {
      label: '字符集',
      prop: 'characterSet',
      value: 'UTF-8',
      type: 'select',
      options: [
        {
          value: 'UTF-8',
          label: 'UTF-8'
        },
        {
          value: 'GBK',
          label: 'GBK'
        }
      ],
      placeholder: '请选择字符集'
    },
    {
      label: 'Schema',
      prop: 'schemasName',
      value: '',
      type: 'input',
      isWidth: true,
      placeholder: '请输入Schema'
    }
  ],
  Sybase: [
    {
      label: 'IP',
      prop: 'ip',
      value: '',
      type: 'input',
      placeholder: '请输入IP'
    },
    {
      label: '端口',
      prop: 'post',
      value: '',
      type: 'input',
      placeholder: '默认端口5000'
    },
    {
      label: '数据库用户名',
      prop: 'userName',
      value: '',
      type: 'input',
      placeholder: '请输入数据库用户名'
    },
    {
      label: '数据库密码',
      prop: 'password',
      value: '',
      type: 'password',
      placeholder: '请输入数据库密码'
    },
    {
      label: '数据库名',
      prop: 'dbname',
      value: '',
      type: 'input',
      placeholder: '请输入数据库名'
    },
    {
      label: '字符集',
      prop: 'characterSet',
      value: 'UTF-8',
      type: 'select',
      options: [
        {
          value: 'UTF-8',
          label: 'UTF-8'
        },
        {
          value: 'GBK',
          label: 'GBK'
        }
      ],
      placeholder: '请选择字符集'
    }
  ],
  Teradata: [
    {
      label: 'IP',
      prop: 'ip',
      value: '',
      type: 'input',
      placeholder: '请输入IP'
    },
    {
      label: '端口',
      prop: 'post',
      value: '',
      type: 'input',
      placeholder: '默认端口1025'
    },
    {
      label: '数据库用户名',
      prop: 'userName',
      value: '',
      type: 'input',
      placeholder: '请输入数据库用户名'
    },
    {
      label: '数据库密码',
      prop: 'password',
      value: '',
      type: 'password',
      placeholder: '请输入数据库密码'
    },
    {
      label: '数据库名',
      prop: 'dbname',
      value: '',
      type: 'input',
      placeholder: '请输入数据库名'
    },
    {
      label: '字符集',
      prop: 'characterSet',
      value: 'UTF-8',
      type: 'select',
      options: [
        {
          value: 'UTF-8',
          label: 'UTF-8'
        },
        {
          value: 'GBK',
          label: 'GBK'
        }
      ],
      placeholder: '请选择字符集'
    }
  ],
  MongoDB: [
    {
      label: 'IP',
      prop: 'ip',
      value: '',
      type: 'input',
      placeholder: '请输入IP'
    },
    {
      label: '端口',
      prop: 'post',
      value: '',
      type: 'input',
      placeholder: '默认端口27017'
    },
    {
      label: '数据库用户名',
      prop: 'userName',
      value: '',
      type: 'input',
      placeholder: '请输入数据库用户名'
    },
    {
      label: '数据库密码',
      prop: 'password',
      value: '',
      type: 'password',
      placeholder: '请输入数据库密码'
    },
    {
      label: '数据库名',
      prop: 'dbname',
      value: '',
      type: 'input',
      isWidth: true,
      placeholder: '请输入数据库名'
    }
  ],
  Ftp: [
    {
      label: '协议类型',
      prop: 'type',
      value: 'FTP',
      type: 'radio',
      options: [
        {
          text: 'FTP',
          value: '0'
        },
        {
          text: 'SFTP',
          value: '1'
        }
      ],
      isWidth: true,
    },
    {
      label: 'IP',
      prop: 'ip',
      value: '',
      type: 'input',
      placeholder: '请输入IP'
    },
    {
      label: '端口',
      prop: 'post',
      value: '',
      type: 'input',
      placeholder: '默认端口21'
    },
    {
      label: '用户名',
      prop: 'userName',
      value: '',
      type: 'input',
      placeholder: '请输入用户名'
    },
    {
      label: '密码',
      prop: 'password',
      value: '',
      type: 'password',
      placeholder: '请输入密码'
    }
  ],
  Hbase: [
    {
      label: 'IP',
      prop: 'zkQuorum',
      value: '',
      type: 'input',
      placeholder: 'Zookeeper集群地址（Host或者IP）'
    },
    {
      label: '端口',
      prop: 'post',
      value: '',
      type: 'input',
      placeholder: '默认端口2181'
    },
    {
      label: '表空间',
      prop: 'dbname',
      value: '',
      type: 'input',
      isWidth: true,
      placeholder: '请输入表空间'
    }
  ],
  HDFS: [
    {
      label: 'DefaultFS',
      prop: 'defaultFs',
      value: '',
      type: 'input',
      isWidth: true,
      placeholder: '格式：hdfs://ServerIP:Port'
    },
    {
      label: '用户名',
      prop: 'userName',
      value: '',
      type: 'input',
      placeholder: '请输入用户名'
    },
    {
      label: 'Kerberos',
      prop: 'haveKerberos',
      value: 0,
      type: 'radio',
      options: [
        {
          text: '关闭',
          value: 0
        },
        {
          text: '开启',
          value: 1
        }
      ],
    }
  ],
  Hive: [
    {
      label: 'JDBC URL',
      prop: 'url',
      value: '',
      type: 'input',
      isWidth: true,
      placeholder: '请输入hive的JDBC连接地址，如 jdbc:hive2://host:port/dbName'
    },
    {
      label: '用户名',
      prop: 'userName',
      value: '',
      type: 'input',
      placeholder: '请输入用户名'
    },
    {
      label: '密码',
      prop: 'password',
      value: '',
      type: 'password',
      placeholder: '请输入密码'
    },
    {
      label: 'DefaultFS',
      prop: 'defaultFs',
      value: '',
      type: 'input',
      isWidth: true,
      placeholder: '格式：hdfs://ServerIP:Port'
    },
    {
      label: 'Warehouse.dir',
      prop: 'warehouseDir',
      value: '',
      type: 'input',
      isWidth: true,
      placeholder: '格式必须为绝对路径，如：/usr/hive/warehouse/dbName'
    },
    {
      label: 'Kerberos',
      prop: 'haveKerberos',
      value: 0,
      type: 'radio',
      options: [
        {
          text: '关闭',
          value: 0
        },
        {
          text: '开启',
          value: 1
        }
      ],
      isWidth: true
    }
  ],
  Impala: [
    {
      label: 'JDBC URL',
      prop: 'url',
      value: '',
      type: 'input',
      isWidth: true,
      placeholder: '请输入hive的JDBC连接地址，如 jdbc:hive2://host:port/dbName'
    },
    {
      label: '用户名',
      prop: 'userName',
      value: '',
      type: 'input',
      placeholder: '请输入用户名'
    },
    {
      label: '密码',
      prop: 'password',
      value: '',
      type: 'password',
      placeholder: '请输入密码'
    },
    {
      label: 'DefaultFS',
      prop: 'defaultFs',
      value: '',
      type: 'input',
      isWidth: true,
      placeholder: '格式：hdfs://ServerIP:Port'
    },
    {
      label: 'Warehouse.dir',
      prop: 'warehouseDir',
      value: '',
      type: 'input',
      isWidth: true,
      placeholder: '格式必须为绝对路径，如：/usr/hive/warehouse/dbName'
    },
    {
      label: 'Kerberos',
      prop: 'haveKerberos',
      value: 0,
      type: 'radio',
      options: [
        {
          text: '关闭',
          value: 0
        },
        {
          text: '开启',
          value: 1
        }
      ],
      isWidth: true
    }
  ]
}
