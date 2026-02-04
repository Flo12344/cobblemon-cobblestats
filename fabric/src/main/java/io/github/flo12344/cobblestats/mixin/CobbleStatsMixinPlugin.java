package io.github.flo12344.cobblestats.mixin;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class CobbleStatsMixinPlugin implements IMixinConfigPlugin{
  @Override
  public void onLoad(String s){

  }

  @Override
  public String getRefMapperConfig(){
    return "";
  }

  @Override
  public boolean shouldApplyMixin(String targetClassName, String mixinClassName){
    if(mixinClassName.startsWith("io.github.flo_12344.cobblestats.common.compat.cobblemonraiddens")){
      return FabricLoader.getInstance().isModLoaded("cobblemonraiddens");
    }
    return true;
  }

  @Override
  public void acceptTargets(Set<String> set, Set<String> set1){

  }

  @Override
  public List<String> getMixins(){
    return List.of();
  }

  @Override
  public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo){

  }

  @Override
  public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo){

  }
}
