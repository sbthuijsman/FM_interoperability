$var_ConsumerName="lsat-consumer"
$var_ProviderName=@("sdf3-provider")
$var_ServiceDefinitionName=@("sdf3-provider")

$var_Consumerid=0
$var_Providerid=0
$var_ServiceDefId=0
$var_InterfaceId=0

$var_ServiceRegistryURI='http://SERVERADDRESS:PORT/serviceregistry/mgmt/systems?direction=ASC&sort_field=id'
$var_httpmethod='GET'
$var_certthumbprint='46FEB8B144FB637B46695F245469F2C8727DC0C3'

$request=iwr -uri $var_ServiceRegistryURI -method $var_httpmethod
$parse=$request.Content | ConvertFrom-Json


foreach ($name in $parse.data) {
    if ( $name.systemName -match $var_ConsumerName ) {    
        $var_Consumerid=$name.id           
    }elseif ($name.systemName -match $var_ProviderName){
        $var_Providerid=$name.id
    }
       
}

$var_ServiceRegistryURI='http://SERVERADDRESS:PORT/serviceregistry/mgmt/services?direction=ASC&sort_field=id'
$var_httpmethod='GET'


$request=iwr -uri $var_ServiceRegistryURI -method $var_httpmethod
$parse=$request.Content | ConvertFrom-Json

foreach ($name in $parse.data) {
    if ( $name.serviceDefinition -match $var_ServiceDefinitionName ) {    
        $var_ServiceDefId=$name.id           
    }
       
}

$var_ServiceRegistryURI='http://SERVERADDRESS:PORT/serviceregistry/mgmt/servicedef/'+$var_ServiceDefinitionName+'?direction=ASC&sort_field=id'
$var_httpmethod='GET'


$request=iwr -uri $var_ServiceRegistryURI -method $var_httpmethod
$parse=$request.Content | ConvertFrom-Json

foreach ($name in $parse.data) {
    if ( $name.serviceDefinition.serviceDefinition -match $var_ServiceDefinitionName ) {    
        $var_InterfaceId=$name.interfaces.id           
    }
       
}

$POSTform=@{
  "consumerId"=$var_Consumerid;
  "interfaceIds"= @($var_InterfaceId);
  "serviceDefinitionIds"= @($var_ServiceDefId);
  "providerIds"= @($var_Providerid)
}

$var_ServiceRegistryURI="http://SERVERADDRESS:PORT/authorization/mgmt/intracloud"
$var_httpmethod='POST'


$POSTformJSON=$POSTform|ConvertTo-Json
$POSTformJSON

iwr -uri $var_ServiceRegistryURI -method $var_httpmethod -Body $POSTformJSON -ContentType "application/json"
