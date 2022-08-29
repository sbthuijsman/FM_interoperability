package myheadlessplugin;


import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import de.christophseidl.util.ecore.EcoreIOUtil;

import org.deltaecore.feature.configuration.DEConfiguration;

import org.deltaecore.feature.variant.DEConfigurationVariantGenerator;
import org.deltaecore.suite.variant.DEConfigurationDeltaModuleVariantGenerator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;



public class genVarcommand {

	private DEConfigurationVariantGenerator variantCreator;
	
	public genVarcommand() {
		variantCreator = new DEConfigurationDeltaModuleVariantGenerator();
	}
	
	public void executeGeneration() {

		System.out.print("Basladi \n");
		
		IFolder folder = getConfigFolder();
		
		//IFile configurationModelFile = folder.getFile("Twilight.deconfiguration");
		IFile configurationModelFile = folder.getFile("Xcps.deconfiguration");
		
		System.out.print("config fullpath: \"" + configurationModelFile.getFullPath() + "\" . \n");
		System.out.print("config name: \"" + configurationModelFile.getName() + "\" . \n");
		
		DEConfiguration configuration = EcoreIOUtil.loadModel(configurationModelFile);
		System.out.print("config model loaded \n");

		deriveVariantFromConfiguration(configuration);
		System.out.print("Perfect! \n");
		
	}
	
	private IFolder getVariantFolder() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot workspaceRoot = workspace.getRoot();
		System.out.print("gc workspaceRoot Loc \"" + workspaceRoot.getLocation() + "\" . \n");
		//IPath workspaceLocation = workspaceRoot.getLocation();
		//IPath path = new Path("D:\\DeltaEcore\\Workspaces\\RuntimeRuntime\\TwilightWithPerfVP\\variant\\");
		//IPath path = new Path("\\TwilightWithPerf\\variant\\");
		IPath path = new Path("\\xCPS\\variant\\");
		//File workspaceDirectory = workspaceLocation.toFile();
		IFolder folder = workspaceRoot.getFolder(path); 
		System.out.print("gc folder \"" + folder.getFullPath() + "\" as variant folder. \n");
		//new Path("/"+workspaceProject.getName()+to+"/"+resourceName)
				
		return folder;
	}
	
	private IFolder getConfigFolder() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot workspaceRoot = workspace.getRoot();
		System.out.print("workspaceRoot Loc \"" + workspaceRoot.getLocation() + "\" . \n");
		//IPath path = new Path("\\TwilightWithPerfVP\\");
		//IPath path = new Path("\\TwilightWithPerf\\config\\");
		IPath path = new Path("\\xCPS\\config\\");

		IFolder folder = workspaceRoot.getFolder(path); 
		System.out.print("folder \"" + folder.getFullPath() + "\" as config folder. \n");
				
		return folder;
	}
	
	public void deriveVariantFromConfiguration(DEConfiguration configuration) {
		
		
		IFolder variantFolder = getVariantFolder();
		//JFaceUtil.alertInformation("Using current folder \"" + variantFolder.getFullPath() + "\" as variant folder.");
			
		try {
				variantCreator.createAndSaveVariantFromConfiguration(configuration, variantFolder);
				System.out.print("Variant derivation completed successfully.");
				//JFaceUtil.alertInformation("Variant derivation completed successfully.");
			} catch (Exception e) {
				System.out.print("An error occurred: " + e.getMessage());
				//JFaceUtil.alertInformation("An error occurred: " + e.getMessage());
			}
	}
	
}
